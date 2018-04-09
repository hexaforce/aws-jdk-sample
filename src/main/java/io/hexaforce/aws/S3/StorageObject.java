package io.hexaforce.aws.S3;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @author tantaka
 *
 */
@Data
public class StorageObject {
	
	private String bucketName;
	private String key;

	private File requestByFile;
	private StringBuffer requestByBuffer;
	private InputStream requestByStream;
	
	private InputStream responseContents;
	
	private Date lastModified;
	private String versionId;
	
}