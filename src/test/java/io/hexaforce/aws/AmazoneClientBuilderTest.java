package io.hexaforce.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.amazonaws.services.s3.model.Bucket;

import io.hexaforce.aws.S3.SimpleStorageService;

public class AmazoneClientBuilderTest {
	
	@Test
	@DisplayName("My 1st JUnit 5 test! 😎")
	void myFirstTest(TestInfo testInfo) {
		// Calculator calculator = new Calculator();
//		 assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
		
		
		SimpleStorageService s3 = new SimpleStorageService();
		
		for (Bucket b :s3.listBucket()) {
			System.out.println(b);
		}
		
		 assertEquals("My 1st JUnit 5 test! 😎", testInfo.getDisplayName(), () ->
		 "TestInfo is injected correctly");
	}
	
}
