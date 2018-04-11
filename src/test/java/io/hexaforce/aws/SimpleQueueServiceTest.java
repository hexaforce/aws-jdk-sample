package io.hexaforce.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import io.hexaforce.aws.SNS.SESNotificationAnalyzer;
import io.hexaforce.aws.SQS.QueueObject;
import io.hexaforce.aws.SQS.SimpleQueueService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleQueueServiceTest {
	
	@Test
	@DisplayName("My 1st JUnit 5 test! 😎")
	void sqsTest(TestInfo testInfo) {
		
		// Calculator calculator = new Calculator();
		// assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
		
		String requestUrl = "no-reply-mail-bunkyo";
		for (QueueObject q :SimpleQueueService.receiveMessage(requestUrl)) {
//			JsonNode x = jackson.readTree(message.getBody()).get("Message");

			SESNotificationAnalyzer.analyze(q.getBody());
			log.info("Q:{}",q);
		}

		
		assertEquals("My 1st JUnit 5 test! 😎", testInfo.getDisplayName(), () -> "TestInfo is injected correctly");
	}

}
