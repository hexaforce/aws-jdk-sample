package io.hexaforce.aws.SES;

import lombok.Data;

@Data
public class EmailObject {

	private String from;
	private String to;
	private String subject;
	private String body;

	private int resultHttpStatusCode;
	private String resultMessageId;
	private String resultRequestId;
	
}