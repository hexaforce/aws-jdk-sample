package io.hexaforce.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 * 
 * @author tantaka
 *
 */
public class AmazoneClientBuilder {

	final static ProfileCredentialsProvider credentialsProviderS3 = new ProfileCredentialsProvider("S3");
	
	/**
	 * @return
	 */
	protected static AmazonS3 buildS3Client() {

		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials).
		 */
		try {

			AWSCredentials credentials = credentialsProviderS3.getCredentials();
			AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
			AmazonS3 s3 = AmazonS3ClientBuilder.standard()
					.withCredentials(provider)
					.withRegion(Regions.AP_NORTHEAST_1)
					.build();
			return s3;

		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct location (~/.aws/credentials), and is in valid format.",
					e);
		}

	}
	
	/**
	 * @return
	 */
	protected static AmazonS3 buildEncryptionS3Client() {

		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials).
		 */
		try {

			AWSCredentials credentials = credentialsProviderS3.getCredentials();
			AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
			AmazonS3 s3 = AmazonS3EncryptionClientBuilder.standard()
					.withCredentials(provider)
					.withRegion(Regions.AP_NORTHEAST_1)
					.build();
			return s3;

		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct location (~/.aws/credentials), and is in valid format.",
					e);
		}

	}
	final static ProfileCredentialsProvider credentialsProviderSES = new ProfileCredentialsProvider("SES");
	
	/**
	 * @return
	 */
	protected static AmazonSimpleEmailService buildSESClient() {

		/*
		 * Before running the code: Fill in your AWS access credentials in the provided
		 * credentials file template, and be sure to move the file to the default
		 * ocation (~/.aws/credentials) where the sample code will load the credentials
		 * from. https://console.aws.amazon.com/iam/home?#security_credential
		 *
		 * WARNING: To avoid accidental leakage of your credentials, DO NOT keep the
		 * credentials file in your source directory.
		 */
		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withCredentials(credentialsProviderSES)
					.withRegion(Regions.US_WEST_2).build();
			return client;
			
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct location (~/.aws/credentials), "
					+ "and is in valid format.", e);
		}

	}

	/**
	 * @return
	 */
	protected static AmazonSimpleEmailServiceAsync buildAsyncSESClient() {

		/*
		 * Before running the code: Fill in your AWS access credentials in the provided
		 * credentials file template, and be sure to move the file to the default
		 * ocation (~/.aws/credentials) where the sample code will load the credentials
		 * from. https://console.aws.amazon.com/iam/home?#security_credential
		 *
		 * WARNING: To avoid accidental leakage of your credentials, DO NOT keep the
		 * credentials file in your source directory.
		 */
		try {
			
			AmazonSimpleEmailServiceAsync client = AmazonSimpleEmailServiceAsyncClientBuilder.standard()
					.withCredentials(credentialsProviderSES)
					.withRegion(Regions.US_WEST_2)
					.build();
			return client;
			
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct location (~/.aws/credentials), "
					+ "and is in valid format.", e);
		}

	}

	final static ProfileCredentialsProvider credentialsProviderSQS = new ProfileCredentialsProvider("SQS");

	/**
	 * @return
	 */
	protected static AmazonSQS buildSQSClient() {

		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials).
		 */
		try {
			
			AmazonSQS sqs = AmazonSQSClientBuilder.standard()
					.withCredentials(credentialsProviderSQS)
					.withRegion(Regions.AP_NORTHEAST_1)
					.build();
			return sqs;

		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct location (~/.aws/credentials), and is in valid format.",
					e);
		}

	}
	
	/**
	 * @return
	 */
	protected static AmazonSQS buildAsyncSQSClient() {

		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials).
		 */
		try {
			
			AmazonSQS sqs = AmazonSQSAsyncClientBuilder.standard()
					.withCredentials(credentialsProviderSQS)
					.withRegion(Regions.AP_NORTHEAST_1)
					.build();
			return sqs;

		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct location (~/.aws/credentials), and is in valid format.",
					e);
		}

	}
	
}