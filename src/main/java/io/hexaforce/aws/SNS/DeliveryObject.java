package io.hexaforce.aws.SNS;

import java.util.List;

import lombok.Data;

@Data
public class DeliveryObject {

	private String timestamp;
	private String processingTimeMillis;
	private List<String> recipients;
	private String smtpResponse;
	private String remoteMtaIp;
	private String reportingMTA;

}
