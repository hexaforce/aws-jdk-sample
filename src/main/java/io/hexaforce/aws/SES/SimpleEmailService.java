package io.hexaforce.aws.SES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import io.hexaforce.aws.AmazoneClientBuilder;
import io.hexaforce.history.HistoryType;
import io.hexaforce.history.MailSendHistory;
import io.hexaforce.history.MailSendHistoryRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tantaka
 *
 */
@Slf4j
@Component
public class SimpleEmailService extends AmazoneClientBuilder {

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	/**
	 * メールを送信します
	 * 
	 * @param value
	 * @return
	 */
	public EmailObject sendEmail(EmailObject mail) {
		return sendEmail(Arrays.asList(mail)).get(0);
	}

	/**
	 * メールを非同期で送信します
	 * 
	 * @param value
	 * @return
	 */
	public EmailObject sendAsyncEmail(EmailObject mail) {
		return sendAsyncEmail(Arrays.asList(mail)).get(0);
	}

	/**
	 * 複数のメールを送信します
	 * 
	 * @param values
	 * @return
	 */
	public List<EmailObject> sendEmail(List<EmailObject> mails) {
		AmazonSimpleEmailService client = buildSESClient();
		return send(client, mails);
	}

	/**
	 * 複数のメールを非同期で送信します
	 * 
	 * @param values
	 * @return
	 */
	public List<EmailObject> sendAsyncEmail(List<EmailObject> mails) {
		AmazonSimpleEmailServiceAsync client = buildAsyncSESClient();
		return send(client, mails);
	}

	/**
	 * メールを送信します
	 * 
	 * @param client
	 * @param mails
	 * @return
	 */
	private  List<EmailObject> send(AmazonSimpleEmailService client, List<EmailObject> mails) {

		List<EmailObject> result = new ArrayList<>();
		List<MailSendHistory> historyList = new ArrayList<>();

		for (EmailObject mail : mails) {

			try {

				// Assemble the SendEmailRequest.
				SendEmailRequest request = toSendEmailResult(mail);

				// Send the email.
				SendEmailResult sendEmailResult = client.sendEmail(request);

				mail.setHttpStatusCode(sendEmailResult.getSdkHttpMetadata().getHttpStatusCode());
				mail.setMessageId(sendEmailResult.getMessageId());
				mail.setRequestId(sendEmailResult.getSdkResponseMetadata().getRequestId());
				result.add(mail);
				log.info("Email sent! :{}", mail);
				
				// 送信履歴
				MailSendHistory history = new MailSendHistory();
				history.setId(mail.getMessageId());
				history.setEmailAddress(mail.getTo());
				history.setHistoryType(HistoryType.Undetermined);
				history.setSubject(history.getSubject());
				historyList.add(history);
				
			} catch (Exception e) {
				displayException(e);
			}

		}
		
		// 履歴保存
		mailSendHistoryRepository.saveAll(historyList);
		
		return result;

	}

	/**
	 * メールのリクエストを作成します
	 * 
	 * @param mail
	 * @return
	 */
	private SendEmailRequest toSendEmailResult(EmailObject mail) {

		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[] { mail.getTo() });

		// Create the subject and body of the message.
		Content subject = new Content().withData(mail.getSubject());
		Content textBody = new Content().withData(mail.getBody());
		Body body = new Body().withText(textBody);

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subject).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(mail.getFrom()).withDestination(destination)
				.withMessage(message);

		return request;

	}

	/**
	 * @param e
	 */
	private void displayException(Exception e) {

		log.error("The email was not sent.");
		log.error("Error message: " + e.getMessage());

	}

}