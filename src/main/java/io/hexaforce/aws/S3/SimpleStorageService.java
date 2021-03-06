package io.hexaforce.aws.S3;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import io.hexaforce.aws.AmazoneClientBuilder;

/**
 * @author tantaka
 *
 */
@Component
public class SimpleStorageService extends AmazoneClientBuilder {

	/**
	 * バケットを作成します
	 * 
	 * @param bucketName
	 * @return Bucket
	 */
	public StorageObject createBucket(String bucketName) {
		return createBucket(Arrays.asList(bucketName)).get(0);
	}

	/**
	 * 複数のバケットを作成します
	 * 
	 * @param bucketNames
	 * @return
	 */
	public List<StorageObject> createBucket(List<String> bucketNames) {
		AmazonS3 s3 = buildS3Client();
		List<StorageObject> result = new ArrayList<>();
		for (String bucketName : bucketNames) {
			try {
				Bucket bucket = s3.createBucket(bucketName);
				StorageObject o = new StorageObject();
				BeanUtils.copyProperties(o, bucket);
				result.add(o);
			} catch (Exception e) {
				displayException(e);
			}
		}
		return result;
	}

	/**
	 * バケットを削除します
	 * 
	 * @param bucketName
	 */
	public void deleteBucket(String bucketName) {
		deleteBucket(Arrays.asList(bucketName));
	}

	/**
	 * 複数のバケットを削除します
	 * 
	 * @param bucketNames
	 */
	public void deleteBucket(List<String> bucketNames) {
		AmazonS3 s3 = buildS3Client();
		for (String bucketName : bucketNames) {
			try {
				s3.deleteBucket(bucketName);
			} catch (Exception e) {
				displayException(e);
			}
		}
	}

	/**
	 * バケット一覧を取得します
	 * 
	 * @return
	 */
	public List<Bucket> listBucket() {
		AmazonS3 s3 = buildS3Client();
		return s3.listBuckets();
	}

	/**
	 * オブジェクトを検索します
	 * 
	 * @param bucketName
	 * @param prefix
	 */
	public List<StorageObject> listObject(String bucketName, String prefix) {
		AmazonS3 s3 = buildS3Client();
		ListObjectsRequest serchRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix);
		List<StorageObject> result = new ArrayList<>();
		try {
			for (S3ObjectSummary summary : s3.listObjects(serchRequest).getObjectSummaries()) {
				StorageObject o = new StorageObject();
				BeanUtils.copyProperties(o, summary);
				result.add(o);
			}
		} catch (Exception e) {
			displayException(e);
		}
		return result;
	}

	/**
	 * オブジェクトを保存(アップロード)します
	 * 
	 * @param value
	 */
	public StorageObject putObject(StorageObject value) {
		return putObject(Arrays.asList(value)).get(0);
	}

	/**
	 * 複数のオブジェクトを保存(アップロード)します
	 * 
	 * @param values
	 * @return
	 */
	public List<StorageObject> putObject(List<StorageObject> values) {
		AmazonS3 s3 = buildS3Client();

		for (StorageObject v : values) {
			try {
				PutObjectResult s = null;
				if (v.getRequestByStream() != null) {
					s = s3.putObject(v.getBucketName(), v.getKey(), v.getRequestByFile());
				} else if (v.getRequestByStream() != null) {
					s = s3.putObject(v.getBucketName(), v.getKey(), v.getRequestByStream(), new ObjectMetadata());
				} else if (v.getRequestByBuffer() != null) {
					s = s3.putObject(v.getBucketName(), v.getKey(), v.getRequestByBuffer().toString());
				}

				if (s != null) {
					v.setLastModified(s.getMetadata().getLastModified());
					v.setVersionId(s.getMetadata().getVersionId());
				}

			} catch (Exception e) {
				displayException(e);
			}
		}

		return values;
	}

	/**
	 * オブジェクトを取得します
	 * 
	 * @param value
	 * @return
	 */
	public StorageObject getObject(StorageObject value) {
		return getObject(Arrays.asList(value)).get(0);
	}

	/**
	 * 複数のオブジェクトを取得します
	 * 
	 * @param values
	 * @return
	 */
	public List<StorageObject> getObject(List<StorageObject> values) {
		AmazonS3 s3 = buildS3Client();
		for (StorageObject v : values) {
			try {
				v.setResponseContents(s3.getObject(v.getBucketName(), v.getKey()).getObjectContent());
			} catch (Exception e) {
				displayException(e);
			}
		}
		return values;
	}

	/**
	 * オブジェクトを削除します
	 * 
	 * @param value
	 */
	public void deleteObject(StorageObject value) {
		deleteObject(Arrays.asList(value));
	}

	/**
	 * 複数のオブジェクトを削除します
	 * 
	 * @param values
	 */
	public void deleteObject(List<StorageObject> values) {
		AmazonS3 s3 = buildS3Client();
		for (StorageObject v : values) {
			try {
				s3.deleteObject(v.getBucketName(), v.getKey());
			} catch (Exception e) {
				displayException(e);
			}
		}

	}

	/**
	 * 例外を出力します
	 * 
	 * @param e
	 * @throws IOException
	 */
	private void displayException(Exception e) {

		if (e instanceof AmazonServiceException) {
			AmazonServiceException ase = (AmazonServiceException) e;
			out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, "
					+ "but was rejected with an error response for some reason.");
			out.println("Error Message:    " + ase.getMessage());
			out.println("HTTP Status Code: " + ase.getStatusCode());
			out.println("AWS Error Code:   " + ase.getErrorCode());
			out.println("Error Type:       " + ase.getErrorType());
			out.println("Request ID:       " + ase.getRequestId());
		} else if (e instanceof AmazonClientException) {
			AmazonClientException ace = (AmazonClientException) e;
			out.println("Caught an AmazonClientException, "
					+ "which means the client encountered a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			out.println("Error Message: " + ace.getMessage());
		} else {
			e.printStackTrace();
		}

	}

	/**
	 * Displays the contents of the specified input stream as text.
	 *
	 * @param input
	 *            The input stream to display as text.
	 *
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			out.println("    " + line);
		}
		out.println("");
	}
}