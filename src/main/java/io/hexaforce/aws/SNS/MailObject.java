package io.hexaforce.aws.SNS;

import java.util.List;

import lombok.Data;

@Data
public class MailObject {
	
	private String timestamp;
	private String messageId;
	private String source;
	private String sourceArn;
	private String sourceIp;
	private String sendingAccountId;
	private List<String> destination;
	private String headersTruncated;
	private String headers;
	private String commonHeaders;
	
}
