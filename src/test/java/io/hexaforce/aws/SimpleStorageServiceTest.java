package io.hexaforce.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.amazonaws.services.s3.model.Bucket;

import io.hexaforce.aws.S3.SimpleStorageService;
import io.hexaforce.aws.SQS.SimpleQueueService;


public class SimpleStorageServiceTest {
	
	@Test
	@DisplayName("My 1st JUnit 5 test! 😎")
	void s3Test(TestInfo testInfo) {
		
		// Calculator calculator = new Calculator();
		// assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");

//		for (Bucket b : SimpleStorageService.listBucket()) {
//			System.out.println(b);
//		}


		assertEquals("My 1st JUnit 5 test! 😎", testInfo.getDisplayName(), () -> "TestInfo is injected correctly");
	}

}
