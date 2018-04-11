package io.hexaforce.aws.SNS;

import lombok.Data;

@Data
public class ComplaintObject {
	
	private String complainedRecipients;
	private String timestamp;
	private String feedbackId;
	private String userAgent;
	private String complaintFeedbackType;
	private String arrivalDate;
	
}
