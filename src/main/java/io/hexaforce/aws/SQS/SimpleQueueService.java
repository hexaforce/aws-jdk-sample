package io.hexaforce.aws.SQS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
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
	 * キューを作成します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject createQueue(String queueName) {
		return createQueue(Arrays.asList(queueName)).get(0);
	}

	/**
	 * 複数のキューを作成します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> createQueue(List<String> queueNames) {
		AmazonSQS sqs = buildSQSClient();
		List<QueueObject> result = new ArrayList<>();
		try {
			for (String queueName : queueNames) {
				CreateQueueResult createQueueResult = sqs.createQueue(queueName);
				QueueObject o = new QueueObject();
				BeanUtils.copyProperties(o, createQueueResult);
				result.add(o);
			}
		} catch (Exception e) {
			displayException(e);
		}
		return result;
	}

	/**
	 * キューを削除します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject deleteQueue(String queueName) {
		return deleteQueue(Arrays.asList(queueName)).get(0);
	}

	/**
	 * 複数のキューを削除します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> deleteQueue(List<String> queueNames) {
		AmazonSQS sqs = buildSQSClient();
		List<QueueObject> result = new ArrayList<>();
		try {
			for (String queueName : queueNames) {
				DeleteQueueResult createQueueResult = sqs.deleteQueue(queueName);
				QueueObject o = new QueueObject();
				BeanUtils.copyProperties(o, createQueueResult);
				result.add(o);
			}
		} catch (Exception e) {
			displayException(e);
		}
		return result;
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
	public static QueueObject sendMessage(String queueName, QueueObject message) {
		return sendMessage(queueName, Arrays.asList(message)).get(0);
	}

	/**
	 * 複数のメッセージを送信します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> sendMessage(String queueName, List<QueueObject> messages) {

		AmazonSQS sqs = buildSQSClient();

		List<QueueObject> result = new ArrayList<>();
		String queueUrl = availabilityQueueUrl(sqs, queueName);
		if (queueUrl == null) {
			return result;
		}

		try {
			for (QueueObject message : messages) {
				SendMessageResult sendMessageResult = sqs.sendMessage(queueUrl, message.getBody());
				QueueObject o = new QueueObject();
				BeanUtils.copyProperties(o, sendMessageResult);
				result.add(o);
			}
		} catch (Exception e) {
			displayException(e);
		}

		return result;

	}

	/**
	 * メッセージを受信します
	 * 
	 * @param queueUrl
	 * @return
	 */
	public static List<QueueObject> receiveMessage(String queueName) {
		
		AmazonSQS sqs = buildSQSClient();
		
		List<QueueObject> result = new ArrayList<>();
		String queueUrl = availabilityQueueUrl(sqs, queueName);
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
	 * メッセージを削除します
	 * 
	 * @param value
	 * @return
	 */
	public static QueueObject deleteMessage(String queueName, QueueObject message) {
		return deleteMessage(queueName, Arrays.asList(message)).get(0);
	}

	/**
	 * 複数のメッセージを削除します
	 * 
	 * @param values
	 * @return
	 */
	public static List<QueueObject> deleteMessage(String queueName, List<QueueObject> messages) {
		
		AmazonSQS sqs = buildSQSClient();
		
		List<QueueObject> result = new ArrayList<>();
		String queueUrl = availabilityQueueUrl(sqs, queueName);
		if (queueUrl == null) {
			return result;
		}
		
		try {
			for (QueueObject message : messages) {
				DeleteMessageResult deleteMessageResult = sqs.deleteMessage(queueUrl, message.getReceiptHandle());
				QueueObject o = new QueueObject();
				o.setQueueUrl(queueUrl);
				BeanUtils.copyProperties(o, deleteMessageResult);
				result.add(o);
			}
		} catch (Exception e) {
			displayException(e);
		}
		
		return result;

	}
	
	/**
	 * 接続URLを返します
	 * @param requestUrl
	 * @return
	 */
	private static String availabilityQueueUrl(AmazonSQS sqs, String queueName) {
		for (String queueUrl : listQueueUrls(sqs)) {
			if (queueUrl.endsWith("/" + queueName)) {
				return queueUrl;
			}
		}
		log.error("It is not a valid queue name. {}", queueName);
		return null;
	}
	
//	public static void main(String[] args) {
//
//		AmazonSQS sqs = buildSQSClient();
//
//		log.info("===========================================");
//		log.info("Getting Started with Amazon SQS");
//		log.info("===========================================\n");
//
//		try {
//
//			// Create a queue
//			log.info("Creating a new SQS queue called MyQueue.\n");
//			CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
//			String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
//
//			// List queues
//			log.info("Listing all queues in your account.\n");
//			for (String queueUrl : sqs.listQueues().getQueueUrls()) {
//				log.info("  QueueUrl: " + queueUrl);
//			}
//			log.info("");
//
//			// Send a message
//			log.info("Sending a message to MyQueue.\n");
//			sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));
//
//			// Receive messages
//			log.info("Receiving messages from MyQueue.\n");
//			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
//			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
//			for (Message message : messages) {
//				log.info("  Message");
//				log.info("    MessageId:     " + message.getMessageId());
//				log.info("    ReceiptHandle: " + message.getReceiptHandle());
//				log.info("    MD5OfBody:     " + message.getMD5OfBody());
//				log.info("    Body:          " + message.getBody());
//				for (Entry<String, String> entry : message.getAttributes().entrySet()) {
//					log.info("  Attribute");
//					log.info("    Name:  " + entry.getKey());
//					log.info("    Value: " + entry.getValue());
//				}
//			}
//			log.info("");
//
//			// Delete a message
//			log.info("Deleting a message.\n");
//			String messageReceiptHandle = messages.get(0).getReceiptHandle();
//			sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
//
//			// Delete a queue
//			log.info("Deleting the test queue.\n");
//			sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
//
//		} catch (AmazonServiceException ase) {
//
//			log.error("Caught an AmazonServiceException, which means your request made it "
//					+ "to Amazon SQS, but was rejected with an error response for some reason.");
//			log.error("Error Message:    " + ase.getMessage());
//			log.error("HTTP Status Code: " + ase.getStatusCode());
//			log.error("AWS Error Code:   " + ase.getErrorCode());
//			log.error("Error Type:       " + ase.getErrorType());
//			log.error("Request ID:       " + ase.getRequestId());
//
//		} catch (AmazonClientException ace) {
//
//			log.error("Caught an AmazonClientException, which means the client encountered "
//					+ "a serious internal problem while trying to communicate with SQS, such as not "
//					+ "being able to access the network.");
//			log.error("Error Message: " + ace.getMessage());
//
//		}
//	}

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