package com.ibm.tw.cloud.os.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.tw.cloud.os.domain.Bucket;
import com.ibm.tw.cloud.os.domain.ListBucketResult;

@Service
public interface CloudObjectStorageService {
	
	/**
	 * List all buckets in Cloud Object Storage Service
	 * @return a List containing buckets
	 */
	public List<Bucket> listBuckets();
	
	/**
	 * List all objects inside the given bucket.
	 * @param bucketname - where the objects stored.
	 * @return a ListBucketResult.
	 */
	public ListBucketResult listObjects(String bucketname);
	
	/**
	 * Create a new Bucket on the Cloud Object Storage Service.
	 * @param bucketname - name of the bucket being created.
	 */
	public void createBucket(String bucketname);
	
	/**
	 * Create a new Object inside the given bucket
	 * @param bucketname - the name of the bucket in which the new object is being created.
	 */
	public void createTextFile(String bucketname, String itemName, String fileText);
	
	/**
	 * Download the object inside the specified bucket and save it into a file.
	 * 
	 * @param bucketname - where the downloaded object is located.
	 * @param objectName - the name of the object being downloaded.
	 * @param filename - the filename of the file that stored the downloaded content.
	 */
	public void downloadObject(String bucketname, String objectName, String filename);
	
	/**
	 * Upload a file to the specified bucket.
	 * @param bucketname - must already exist.
	 * @param objectKey
	 * @param file - cannot be null.
	 */
	public void uploadFile(String bucketname, String objectKey, File file);
	
	/**
	 * Remove the specified object from the specified bucket.
	 * 
	 * @param bucketname - name of the bucket where the object will be removed.
	 * @param objectKey - key of the object to be removed.
	 */
	public void removeObject(String bucketname, String objectKey);
	
	/**
	 * Remove multiple objects from the specified bucket.
	 * 
	 * @param bucketname - name of the bucket where the objects will be removed.
	 * @param objectKeys - keys of the object to be removed.
	 */
	public void removeObject(String bucketname, String... objectKeys);
	
	
	/**
	 * Remove a bucket
	 * @param bucketname - the name of the bucket to be removed.
	 */
	public void removeBucket(String bucketname);
	
	/**
	 * Return the S3 account information 
	 * @return
	 */
	public String info();
}
