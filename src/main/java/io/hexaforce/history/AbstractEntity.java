package io.hexaforce.history;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Oliver Gierke
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	@CreatedDate LocalDateTime createdDate;
	@LastModifiedDate LocalDateTime lastModifiedDate;

}
