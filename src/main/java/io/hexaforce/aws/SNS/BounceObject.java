package io.hexaforce.aws.SNS;

import lombok.Data;

@Data
public class BounceObject {
	
	private String bounceType;
	private String bounceSubType;
	private String bouncedRecipients;
	private String timestamp;
	private String feedbackId;
	private String remoteMtaIp;
	private String reportingMTA;
	private String emailAddress;
	private String action;
	private String status;
	private String diagnosticCode;
	
}
