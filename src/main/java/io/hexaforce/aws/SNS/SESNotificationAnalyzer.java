package io.hexaforce.aws.SNS;

import java.io.IOException;

import com.amazonaws.services.simpleemail.model.NotificationType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SESNotificationAnalyzer {

	public static void analyze(String body) {

		try {

			ObjectMapper jackson = new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode root = trimIndention(jackson, body);

			NotificationType type = checkNotificationType(root);
			if (type == null) {
				return;
			}

			MailObject mailObject = jackson.readValue(root.get("mail").toString(), MailObject.class);
			log.info("TargetMail :{}", mailObject);

			switch (type) {
			
			case Delivery:
				
				DeliveryObject deliveryObject = 
					jackson.readValue(root.get("delivery").toString(), DeliveryObject.class);
				log.info("Delivery :{}", deliveryObject);
				break;
				
			case Bounce:
				
				BounceObject bounceObject = 
					jackson.readValue(root.get("bounce").toString(), BounceObject.class);
				log.warn("Bounce :{}", bounceObject);
				break;
				
			case Complaint:
				
				ComplaintObject complaintObject = 
					jackson.readValue(root.get("complaint").toString(), ComplaintObject.class);
				log.error("Complaint :{}", complaintObject);
				break;
				
			default:

				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param body
	 * @return
	 */
	private static JsonNode trimIndention(ObjectMapper jackson, String body) {
		try {
			JsonNode x = jackson.readTree(body).get("Message");
			return jackson.readTree(x.asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static NotificationType checkNotificationType(JsonNode root) {
		String notificationType = root.get("notificationType").asText();
		try {
			return NotificationType.fromValue(notificationType);
		} catch (IllegalArgumentException e) {
			log.error("This has nothing to do with SES's sending history :{}", notificationType);
		}
		return null;
	}

}
