package io.hexaforce.aws.S3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

import io.hexaforce.aws.AmazoneClientBuilder;

/**
 * [MPU]低レベルAPIファイルトランスファ
 * @author tantaka
 *
 */
public class UploadObjectMPULowLevelAPI extends AmazoneClientBuilder {

	/**
	 * S3サイズ制限を回避して容量の大きなファイルをアップロードします
	 * 
	 * @param existingBucketName
	 * @param keyName
	 * @param file
	 */
	public CompleteMultipartUploadResult upload(String existingBucketName, String keyName, File file) {

		AmazonS3 s3Client = buildS3Client();

		// Create a list of UploadPartResponse objects. You get one of these for each
		// part upload.
		List<PartETag> partETags = new ArrayList<PartETag>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(existingBucketName, keyName);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

		// File file = new File(filePath);
		long contentLength = file.length();
		long partSize = 5242880; // Set part size to 5 MB.

		try {

			// Step 2: Upload parts.
			long filePosition = 0;

			for (int i = 1; filePosition < contentLength; i++) {

				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, (contentLength - filePosition));

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(existingBucketName)
						.withKey(keyName).withUploadId(initResponse.getUploadId()).withPartNumber(i)
						.withFileOffset(filePosition).withFile(file).withPartSize(partSize);

				// Upload part and add response to our list.
				partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;

			}

			// Step 3: Complete.
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(existingBucketName, keyName,
					initResponse.getUploadId(), partETags);

			return s3Client.completeMultipartUpload(compRequest);

		} catch (Exception e) {

			s3Client.abortMultipartUpload(
					new AbortMultipartUploadRequest(existingBucketName, keyName, initResponse.getUploadId()));

		}
		
		return null;

	}

}