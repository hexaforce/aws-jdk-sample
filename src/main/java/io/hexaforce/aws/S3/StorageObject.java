package io.hexaforce.aws.S3;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import com.amazonaws.services.s3.model.Owner;

import lombok.Data;

/**
 * 
 * @author tantaka
 *
 */
@Data
public class StorageObject {
	
	/**************************
	 * Bucket
	 **************************/
	
    /** The name of this S3 bucket */
    private String name = null;

    /** The details on the owner of this bucket */
    private Owner owner = null;

    /** The date this bucket was created */
    private Date creationDate = null;
	
    
	/**************************
	 * S3ObjectSummary
	 **************************/
	
    /** The name of the bucket in which this object is stored */
	private String bucketName;

    /** The key under which this object is stored */
    private String key;

    /** Hex encoded MD5 hash of this object's contents, as computed by Amazon S3 */
    private String eTag;

    /** The size of this object, in bytes */
    private long size;

    /** The date, according to Amazon S3, when this object was last modified */
    private Date lastModified;

    /** The class of storage used by Amazon S3 to store this object */
    private String storageClass;
    
	
	private File requestByFile;
	private StringBuffer requestByBuffer;
	private InputStream requestByStream;
	
	private InputStream responseContents;
	
	private String versionId;
	
}