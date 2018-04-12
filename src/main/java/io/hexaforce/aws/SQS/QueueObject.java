package io.hexaforce.aws.SQS;

import lombok.Data;

/**
 * DTO for AmazonSQS
 * @author tantaka
 *
 */
@Data
public class QueueObject {
	
	private String queueUrl;
	
	/**A unique identifier for the message. */
    private String messageId;
    
    /** An identifier associated with the act of receiving the message. */
    private String receiptHandle;
    
    /** An MD5 digest of the non-URL-encoded message body string. */
    private String mD5OfBody;
    
    /** The message's contents (not URL-encoded). */
    private String body;
    
    /** An MD5 digest of the non-URL-encoded message attribute string. */
    private String mD5OfMessageAttributes;
    
	
	private String requestId;
	private String sequenceNumber;
	private int httpStatusCode;

	
}