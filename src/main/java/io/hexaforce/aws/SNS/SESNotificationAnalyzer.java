package io.hexaforce.aws.SNS;

import java.io.IOException;

import com.amazonaws.services.simpleemail.model.NotificationType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tantaka
 *
 */
@Slf4j
public class SESNotificationAnalyzer {

	/**
	 * SESの通知解析
	 * @param body
	 * @return
	 */
	public static SESNotificationResult analyze(String body) {

		ObjectMapper jackson = new ObjectMapper().configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		try {

			JsonNode root = trimIndention(jackson, body);

			NotificationType type = checkNotificationType(root);
			if (type == null) {
				return null;
			}

			MailObject mailObject = jackson.readValue(root.get("mail").toString(), MailObject.class);
			log.debug("TargetMail :{}", mailObject);

			switch (type) {

			case Delivery:
				String delivery = root.get("delivery").toString();
				DeliveryObject deliveryObject = jackson.readValue(delivery, DeliveryObject.class);
				log.debug("Delivery :{}", deliveryObject);
				
				return new SESNotificationResult(type, mailObject, deliveryObject);

			case Bounce:
				String bounce = root.get("bounce").toString();
				BounceObject bounceObject = jackson.readValue(bounce, BounceObject.class);
				log.warn("Bounce :{}", bounceObject);
				
				return new SESNotificationResult(type, mailObject, bounceObject);

			case Complaint:
				String complaint = root.get("complaint").toString();
				ComplaintObject complaintObject = jackson.readValue(complaint, ComplaintObject.class);
				log.error("Complaint :{}", complaintObject);
				
				return new SESNotificationResult(type, mailObject, complaintObject);

			default:

				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * メッセージ内容を正規化します
	 * @param body
	 * @return
	 */
	private static JsonNode trimIndention(ObjectMapper jackson, String body) {
		try {
			JsonNode message = jackson.readTree(body).get("Message");
			return jackson.readTree(message.asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * SESの送信通知タイプを返します
	 * @param root
	 * @return
	 */
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
