package io.hexaforce.aws.SNS;

import com.amazonaws.services.simpleemail.model.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SESNotificationResult {
	NotificationType type;
	MailObject mail;
	Object notifyInfo;
}
