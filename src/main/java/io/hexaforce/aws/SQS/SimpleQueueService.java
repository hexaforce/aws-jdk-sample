package io.hexaforce.aws.SQS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import io.hexaforce.aws.AmazoneClientBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author tantaka
 *
 */
@Slf4j
public class SimpleQueueService extends AmazoneClientBuilder {

	/**
	 * @param value
	 * @return
	 */
	public static QueueObject createQueue(QueueObject value) {
		return createQueues(Arrays.asList(value)).get(0);
	}

	/**
	 * @param values
	 * @return
	 */
	public static List<QueueObject> createQueues(List<QueueObject> values) {
		AmazonSQS sqs = buildSQSClient();

		return values;
	}

	/**
	 * @param value
	 * @return
	 */
	public static QueueObject deleteQueue(QueueObject value) {
		return deleteQueues(Arrays.asList(value)).get(0);
	}

	/**
	 * @param values
	 * @return
	 */
	public static List<QueueObject> deleteQueues(List<QueueObject> values) {
		AmazonSQS sqs = buildSQSClient();

		return values;
	}

	/**
	 * @return
	 */
	public static List<String> listQueueUrls() {
		AmazonSQS sqs = buildSQSClient();
		return sqs.listQueues().getQueueUrls();
	}

	/**
	 * @param value
	 * @return
	 */
	public static QueueObject sendMessage(QueueObject value) {
		return sendMessage(Arrays.asList(value)).get(0);
	}

	/**
	 * @param values
	 * @return
	 */
	public static List<QueueObject> sendMessage(List<QueueObject> values) {

		AmazonSQS sqs = buildSQSClient();
		for (QueueObject v : values) {
			SendMessageResult result = sqs.sendMessage(v.getQueueUrl(), v.getMessageBody());
			v.setMessageId(result.getMessageId());
			v.setSequenceNumber(result.getSequenceNumber());
			v.setHttpStatusCode(result.getSdkHttpMetadata().getHttpStatusCode());
		}
		return values;

	}

	/**
	 * @param queueUrl
	 * @return
	 */
	public static List<QueueObject> receiveMessage(String queueUrl) {

		AmazonSQS sqs = buildSQSClient();
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		List<QueueObject> queueObjectList = new ArrayList<>();
		for (Message m : messages) {
			QueueObject queueObject = new QueueObject();
			queueObject.setQueueUrl(queueUrl);
			queueObject.setMessageId(m.getMessageId());
			queueObject.setMessageBody(m.getBody());
			queueObject.setReceiptHandle(m.getReceiptHandle());
			queueObjectList.add(queueObject);
		}
		return queueObjectList;

	}

	/**
	 * @param value
	 * @return
	 */
	public static QueueObject deleteMessage(QueueObject value) {
		return deleteMessage(Arrays.asList(value)).get(0);
	}

	/**
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