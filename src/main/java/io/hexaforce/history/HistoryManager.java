package io.hexaforce.history;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import io.hexaforce.aws.SNS.BounceObject;
import io.hexaforce.aws.SNS.ComplaintObject;
import io.hexaforce.aws.SNS.DeliveryObject;
import io.hexaforce.aws.SNS.SESNotificationAnalyzer;
import io.hexaforce.aws.SNS.SESNotificationResult;
import io.hexaforce.aws.SQS.QueueObject;
import io.hexaforce.aws.SQS.SimpleQueueService;

/**
 * 履歴マネージャ
 * 
 * @author tantaka
 *
 */
public class HistoryManager {

	private final DateTimeFormatter SES_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Autowired
	private SimpleQueueService SQS;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;
	

	/**
	 * @throws InterruptedException 
	 * 
	 */
	public void updateHistory() throws InterruptedException {
		
		// メッセージ全取得
		List<QueueObject> queueList = receiveQueue();
		
		if (queueList.isEmpty()) {
			return;
		}
		
		// JSON解析
		List<SESNotificationResult> analyzedList = new ArrayList<>();
		for (QueueObject queue : queueList) {
			SESNotificationResult result = SESNotificationAnalyzer.analyze(queue.getBody());
			if (Objects.nonNull(result)) {
				analyzedList.add(result);
			}
		}

		// messageIdを抽出
		List<String> messageIdList = analyzedList.stream().map(x -> x.getMail().getMessageId()).collect(Collectors.toList());

		// 更新対象のDB履歴を全取得
		List<MailSendHistory> mailSendHistoryList = mailSendHistoryRepository.findAllById(messageIdList);

		// messageIdでMap化
		Map<String, MailSendHistory> mailSendHistoryMap = mailSendHistoryList.stream()
				.collect(Collectors.toMap(MailSendHistory::getId, Function.identity()));
		
		// 更新するデータを取得
		mailSendHistoryList = changeStatus(analyzedList, mailSendHistoryMap);

		// DBをアップデート
		mailSendHistoryList = mailSendHistoryRepository.saveAll(mailSendHistoryList);

		// 処理済みメッセージ削除
		queueList = SQS.deleteMessage("no-reply-mail-bunkyo", queueList);

	}
	
	/**
	 * 更新用データ作成
	 * @param analyzedList
	 * @param mailSendHistoryMap
	 */
	public List<MailSendHistory> changeStatus(List<SESNotificationResult> analyzedList,
			Map<String, MailSendHistory> mailSendHistoryMap) {

		List<MailSendHistory> mailSendHistoryList = new ArrayList<>();

		for (SESNotificationResult analyzed : analyzedList) {

			MailSendHistory mailSendHistory = mailSendHistoryMap.get(analyzed.getMail().getMessageId());

			switch (analyzed.getType()) {

			case Delivery:
				DeliveryObject deliveryObject = (DeliveryObject) analyzed.getNotifyInfo();
				mailSendHistory.historyType = HistoryType.Delivery;
				mailSendHistory.serverTimestamp = LocalDateTime.parse(deliveryObject.getTimestamp(), SES_DATETIME);
				break;

			case Bounce:
				BounceObject bounceObject = (BounceObject) analyzed.getNotifyInfo();
				mailSendHistory.historyType = HistoryType.Bounce;
				mailSendHistory.serverTimestamp = LocalDateTime.parse(bounceObject.getTimestamp(), SES_DATETIME);
				mailSendHistory.bounceType = bounceObject.getBounceType();
				mailSendHistory.bounceSubType = bounceObject.getBounceSubType();
				break;

			case Complaint:
				ComplaintObject complaintObject = (ComplaintObject) analyzed.getNotifyInfo();
				mailSendHistory.historyType = HistoryType.Complaint;
				mailSendHistory.serverTimestamp = LocalDateTime.parse(complaintObject.getTimestamp(), SES_DATETIME);
				break;

			default:

				break;

			}
			
			mailSendHistoryList.add(mailSendHistory);

		}
		
		return mailSendHistoryList;
	}

	/**
	 * 現在ある全てのメッセージを取得します
	 * @return
	 * @throws InterruptedException 
	 */
	private List<QueueObject> receiveQueue() throws InterruptedException {
		
		List<QueueObject> queueListAll = new ArrayList<>(); 
				
		List<QueueObject> queueList = SQS.receiveMessage("no-reply-mail-bunkyo");
		
		while(!queueList.isEmpty()) {
			
			queueListAll.addAll(queueList);

			TimeUnit.SECONDS.sleep(1);
			
			queueList = SQS.receiveMessage("no-reply-mail-bunkyo");
		}
		
		return queueListAll;
		
	}
	
}
