package io.hexaforce.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import io.hexaforce.aws.SES.EmailObject;
import io.hexaforce.aws.SES.SimpleEmailService;

public class SimpleEmailServiceTest {
	
	@Test
	@DisplayName("My 1st JUnit 5 test! 😎")
	void my2FirstTest(TestInfo testInfo) {
		
		EmailObject o = new EmailObject();

//		o.setFrom("jenkins@demo-campus-gate.com");
//		o.setTo("bunkyo.opt@gmail.com");
//		o.setSubject("sendEmail");
//		o.setBody("BodyBodyBodyBodyBodyBodyBodyBodyBodyBody");
//		//o = SimpleEmailService.sendEmail(o);
		
		o = new EmailObject();
		o.setFrom("no-reply@bunkyo.dev-campus-gate.com");
		o.setTo("bunkyo.opt@gmail.com");
		o.setSubject("sendAsyncEmail");
		o.setBody("BodyBodyBodyBodyBodyBodyBodyBodyBodyBody");
		SimpleEmailService.sendAsyncEmail(o);

		assertEquals("My 1st JUnit 5 test! 😎", testInfo.getDisplayName(), () -> "TestInfo is injected correctly");
	}
	
}
