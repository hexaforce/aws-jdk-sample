package io.hexaforce.aws.SQS;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import io.hexaforce.aws.AmazoneClientBuilder;
import io.hexaforce.aws.S3.StorageObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author tantaka
 *
 */
@Slf4j
public class SimpleQueueService extends AmazoneClientBuilder {

	/**
	 * キューを作成します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject createQueue(QueueObject value) {
		return createQueue(Arrays.asList(value)).get(0);
	}

	/**
	 * 複数のキューを作成します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> createQueue(List<QueueObject> values) {
		AmazonSQS sqs = buildSQSClient();

		return values;
	}

	/**
	 * キューを削除します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject deleteQueue(QueueObject value) {
		return deleteQueue(Arrays.asList(value)).get(0);
	}

	/**
	 * 複数のキューを削除します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> deleteQueue(List<QueueObject> values) {
		AmazonSQS sqs = buildSQSClient();

		return values;
	}

	/**
	 * キューの一覧を返します
	 * 
	 * @return
	 */
	public static List<String> listQueueUrls(AmazonSQS sqs) {
		ListQueuesResult result = sqs.listQueues();
		return result.getQueueUrls();
	}

	/**
	 * メッセージを送信します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject sendMessage(QueueObject value) {
		return sendMessage(Arrays.asList(value)).get(0);
	}

	/**
	 * 複数のメッセージを送信します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> sendMessage(List<QueueObject> values) {
		AmazonSQS sqs = buildSQSClient();
		for (QueueObject v : values) {
			SendMessageResult result = sqs.sendMessage(v.getQueueUrl(), "");
			//SendMessageResult result = sqs.sendMessage(v.getQueueUrl(), v.getMessageBody());
			v.setMessageId(result.getMessageId());
			v.setSequenceNumber(result.getSequenceNumber());
			v.setHttpStatusCode(result.getSdkHttpMetadata().getHttpStatusCode());
		}
		return values;

	}

	/**
	 * メッセージを受信します
	 * 
	 * @param queueUrl
	 * @return
	 */
	public static List<QueueObject> receiveMessage(String requestUrl) {
		
		AmazonSQS sqs = buildSQSClient();
		
		List<QueueObject> result = new ArrayList<>();
		String queueUrl = availabilityQueueUrl(sqs, requestUrl);
		if (queueUrl == null) {
			return result;
		}
		
		try {
			
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			
			for (Message message : messages) {

				QueueObject o = new QueueObject();
				o.setQueueUrl(queueUrl);
				BeanUtils.copyProperties(o, message);
				
				result.add(o);
			}
			
		} catch (Exception e) {
			displayException(e);
		}
		
		return result;

	}
	
	/**
	 * 接続URLを返します(妥当性の検証)
	 * @param requestUrl
	 * @return
	 */
	private static String availabilityQueueUrl(AmazonSQS sqs, String requestUrl) {
		for (String queueUrl : listQueueUrls(sqs)) {
			if (queueUrl.endsWith("/" + requestUrl)) {
				return queueUrl;
			}
		}
		log.error("It is not a valid queue name. {}", requestUrl);
		return null;
	}
	
	
	/**
	 * メッセージを削除します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject deleteMessage(QueueObject value) {
		return deleteMessage(Arrays.asList(value)).get(0);
	}

	/**
	 * 複数のメッセージを削除します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> deleteMessage(List<QueueObject> values) {

		AmazonSQS sqs = buildSQSClient();
		for (QueueObject v : values) {
			DeleteMessageResult result = sqs.deleteMessage(v.getQueueUrl(), v.getReceiptHandle());
			v.setRequestId(result.getSdkResponseMetadata().getRequestId());
			v.setHttpStatusCode(result.getSdkHttpMetadata().getHttpStatusCode());
		}
		return values;

	}

	public static void main(String[] args) {

		AmazonSQS sqs = buildSQSClient();

		log.info("===========================================");
		log.info("Getting Started with Amazon SQS");
		log.info("===========================================\n");

		try {

			// Create a queue
			log.info("Creating a new SQS queue called MyQueue.\n");
			CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
			String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

			// List queues
			log.info("Listing all queues in your account.\n");
			for (String queueUrl : sqs.listQueues().getQueueUrls()) {
				log.info("  QueueUrl: " + queueUrl);
			}
			log.info("");

			// Send a message
			log.info("Sending a message to MyQueue.\n");
			sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));

			// Receive messages
			log.info("Receiving messages from MyQueue.\n");
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			for (Message message : messages) {
				log.info("  Message");
				log.info("    MessageId:     " + message.getMessageId());
				log.info("    ReceiptHandle: " + message.getReceiptHandle());
				log.info("    MD5OfBody:     " + message.getMD5OfBody());
				log.info("    Body:          " + message.getBody());
				for (Entry<String, String> entry : message.getAttributes().entrySet()) {
					log.info("  Attribute");
					log.info("    Name:  " + entry.getKey());
					log.info("    Value: " + entry.getValue());
				}
			}
			log.info("");

			// Delete a message
			log.info("Deleting a message.\n");
			String messageReceiptHandle = messages.get(0).getReceiptHandle();
			sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));

			// Delete a queue
			log.info("Deleting the test queue.\n");
			sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));

		} catch (AmazonServiceException ase) {

			log.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {

			log.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not "
					+ "being able to access the network.");
			log.error("Error Message: " + ace.getMessage());

		}
	}

	private static void displayException(Exception e) {

		if (e instanceof AmazonServiceException) {
			AmazonServiceException ase = (AmazonServiceException) e;
			log.error("Caught an AmazonServiceException, which means your request made it to Amazon SQS, "
					+ "but was rejected with an error response for some reason.");
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());

		} else if (e instanceof AmazonClientException) {
			AmazonClientException ace = (AmazonClientException) e;
			log.error("Caught an AmazonClientException, "
					+ "which means the client encountered a serious internal problem while trying to communicate with SQS, "
					+ "such as not being able to access the network.");
			log.error("Error Message: " + ace.getMessage());
		} else {
			e.printStackTrace();
		}

	}
}