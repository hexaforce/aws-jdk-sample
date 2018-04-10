package io.hexaforce.history;

import java.time.LocalDate;




public class BaseEntity {
	private static final long serialVersionUID = 1L;
	
	private LocalDate createdAt;
	
	private LocalDate updatedAt;
	
	private Boolean deleted;
}
