package io.hexaforce.history;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@Table(name = "mail_send_history")
public class OperationHistory extends AbstractEntity {
	private String deleted;
	private String emailAddress;
	private String notificationType;
	private String subject;
	private String serverTimestamp;
	private String bounceType;
	private String bounceSubType;
	private String retryCount;
	private String nextRetry;
}
