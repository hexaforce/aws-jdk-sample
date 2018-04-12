package io.hexaforce.history;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
public class MailSendHistory extends AbstractEntity {
	String emailAddress;
	@Enumerated(EnumType.STRING) HistoryType historyType;
	String subject;
	LocalDateTime serverTimestamp;
	String bounceType;
	String bounceSubType;
	Integer retryCount;
	LocalDateTime nextRetry;
}
