package io.hexaforce.aws.SES;

import lombok.Data;

/**
 * DTO for AmazonSES
 * @author tantaka
 *
 */
@Data
public class EmailObject {
	
	/**************************
	 * Request
	 **************************/
	private String from;
	private String to;
	private String subject;
	private String body;

	/**************************
	 * Response
	 **************************/
	private int httpStatusCode;
	private String messageId;
	private String requestId;
	
}