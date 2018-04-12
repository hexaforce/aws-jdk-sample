package io.hexaforce.history;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.hexaforce.aws.SNS.SESNotificationAnalyzer;
import io.hexaforce.aws.SNS.SESNotificationResult;
import io.hexaforce.aws.SQS.QueueObject;
import io.hexaforce.aws.SQS.SimpleQueueService;

/**
 * 操作履歴マネージャ
 * 
 * @author tantaka
 *
 */
public class HistoryManager {

	@Autowired
	private SimpleQueueService SQS;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	/**
	 * 
	 */
	public void updateHistory() {
		
		List<QueueObject> queueList = SQS.receiveMessage("no-reply-mail-bunkyo");
		List<SESNotificationResult> resultList = new ArrayList<>();
		
		for (QueueObject queue : queueList) {
			
			SESNotificationResult result = SESNotificationAnalyzer.analyze(queue.getBody());
			switch (result.getType()) {
			
			case Bounce:
				break;
				
			case Complaint:
				break;
				
			case Delivery:
				break;
				
			default:
				break;
			
			}
			
			resultList.add(result);
		}
		
		
	}

}
