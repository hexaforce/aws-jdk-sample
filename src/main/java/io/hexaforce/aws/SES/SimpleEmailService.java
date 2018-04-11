package io.hexaforce.aws.SES;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import io.hexaforce.aws.AmazoneClientBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tantaka
 *
 */
@Slf4j
public class SimpleEmailService extends AmazoneClientBuilder {

	/**
	 * メールを送信します
	 * 
	 * @param value
	 * @return
	 */
	public static EmailObject sendEmail(EmailObject value) {
		return sendEmail(Arrays.asList(value)).get(0);
	}

	/**
	 * メールを非同期で送信します
	 * 
	 * @param value
	 * @return
	 */
	public static void sendAsyncEmail(EmailObject value) {
		sendAsyncEmail(Arrays.asList(value));
	}

	/**
	 * 複数のメールを送信します
	 * 
	 * @param values
	 * @return
	 */
	public static List<EmailObject> sendEmail(List<EmailObject> values) {
		AmazonSimpleEmailService client = buildSESClient();
		return send(client, values);
	}

	/**
	 * 複数のメールを非同期で送信します
	 * 
	 * @param values
	 * @return
	 */
	public static List<EmailObject> sendAsyncEmail(List<EmailObject> values) {
		AmazonSimpleEmailServiceAsync client = buildAsyncSESClient();
		return send(client, values);
	}

	/**
	 * メールを送信します
	 * @param client
	 * @param values
	 * @return
	 */
	private static List<EmailObject> send(AmazonSimpleEmailService client, List<EmailObject> values) {
		
		for (EmailObject mail : values) {

			try {

				// Assemble the SendEmailRequest.
				SendEmailRequest request = toSendEmailResult(mail);

				// Send the email.
				SendEmailResult result = client.sendEmail(request);

				mail.setHttpStatusCode(result.getSdkHttpMetadata().getHttpStatusCode());
				mail.setMessageId(result.getMessageId());
				mail.setRequestId(result.getSdkResponseMetadata().getRequestId());
				log.info("Email sent! :{}",mail);

			} catch (Exception ex) {

				log.error("The email was not sent.");
				log.error("Error message: " + ex.getMessage());

			}

		}
		return values;
	}

	/**
	 * メールのリクエストを作成します
	 * 
	 * @param mail
	 * @return
	 */
	private static SendEmailRequest toSendEmailResult(EmailObject mail) {

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

}