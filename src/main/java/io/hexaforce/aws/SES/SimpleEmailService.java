package io.hexaforce.aws.SES;

import static java.lang.System.out;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import io.hexaforce.aws.AmazoneClientBuilder;

/**
 * @author tantaka
 *
 */
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
	 * 複数のメールを送信します
	 * 
	 * @param values
	 * @return
	 */
	public static List<EmailObject> sendEmail(List<EmailObject> values) {

		AmazonSimpleEmailService client = buildSESClient();

		for (EmailObject mail : values) {

			try {

				// Construct an object to contain the recipient address.
				Destination destination = new Destination().withToAddresses(new String[] { mail.getTo() });

				// Create the subject and body of the message.
				Content subject = new Content().withData(mail.getSubject());
				Content textBody = new Content().withData(mail.getBody());
				Body body = new Body().withText(textBody);

				// Create a message with the specified subject and body.
				Message message = new Message().withSubject(subject).withBody(body);

				// Assemble the email.
				SendEmailRequest request = new SendEmailRequest().withSource(mail.getFrom())
						.withDestination(destination).withMessage(message);

				// Send the email.
				SendEmailResult result = client.sendEmail(request);

				mail.setResultHttpStatusCode(result.getSdkHttpMetadata().getHttpStatusCode());
				mail.setResultMessageId(result.getMessageId());
				mail.setResultRequestId(result.getSdkResponseMetadata().getRequestId());
				out.println("Email sent!");

			} catch (Exception ex) {

				out.println("The email was not sent.");
				out.println("Error message: " + ex.getMessage());

			}

		}

		return values;

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
	 * 複数のメールを非同期で送信します
	 * 
	 * @param values
	 * @return
	 */
	public static void sendAsyncEmail(List<EmailObject> values) {

		AmazonSimpleEmailServiceAsync client = buildAsyncSESClient();

		for (EmailObject mail : values) {

			try {

				// Construct an object to contain the recipient address.
				Destination destination = new Destination().withToAddresses(new String[] { mail.getTo() });

				// Create the subject and body of the message.
				Content subject = new Content().withData(mail.getSubject());
				Content textBody = new Content().withData(mail.getBody());
				Body body = new Body().withText(textBody);

				// Create a message with the specified subject and body.
				Message message = new Message().withSubject(subject).withBody(body);

				// Assemble the email.
				SendEmailRequest request = new SendEmailRequest().withSource(mail.getFrom())
						.withDestination(destination).withMessage(message);

				// Send the email.
				Future<SendEmailResult> result = client.sendEmailAsync(request);

				// mail.setResultHttpStatusCode(result.getSdkHttpMetadata().getHttpStatusCode());
				// mail.setResultMessageId(result.getMessageId());
				// mail.setResultRequestId(result.getSdkResponseMetadata().getRequestId());
				out.println("Email sent!");

			} catch (Exception ex) {

				out.println("The email was not sent.");
				out.println("Error message: " + ex.getMessage());

			}

		}

	}

}