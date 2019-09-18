package org.elasticsearch.aliyun.oss.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * OSS Service interface for creating oss client.
 * Created by yangkongshi on 2017/11/24.
 */
public interface OssService {

    /**
     * Bulk delete the specified bucket {@link OSSObject}.
     *
     * @param deleteObjectsRequest request parameters {@link DeleteObjectsRequest}
     * @return delete results in bulk.
     */
    DeleteObjectsResult deleteObjects(DeleteObjectsRequest deleteObjectsRequest)
            throws OSSException, ClientException;

    /**
     * Determines if the specified {@link OSSObject} exists under the specified {@link Bucket}.
     *
     * @param bucketName bucket name
     * @param key        Object Key
     * @return {@code true} if a blob exists with the given name, and {@code false} otherwise.
     */
    boolean doesObjectExist(String bucketName, String key) throws OSSException, ClientException;


    /**
     * Determines if a given {@link Bucket} exists.
     *
     * @param bucketName bucket name
     */
    boolean doesBucketExist(String bucketName) throws OSSException, ClientException;

    /**
     * List {@link OSSObject} under the specified {@link Bucket}.
     *
     * @param listObjectsRequest request information
     * @return object list {@link ObjectListing}
     */
    ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
            throws OSSException, ClientException;

    /**
     * Export {@link OSSObject} from the OSS specified {@link Bucket}.
     *
     * @param bucketName Bucket name.
     * @param key        Object Key.
     * @return Request result {@link OSSObject} instance. After use, you need to manually
     * close the ObjectContent release request connection.
     */
    OSSObject getObject(String bucketName, String key)
            throws OSSException, ClientException, IOException;

    /**
     * Upload the specified {@link OSSObject} to the {@link Bucket} specified in OSS.
     *
     * @param bucketName Bucket name
     * @param key        object key
     * @param input      inputStream
     * @param metadata   the meta-information of the object {@link ObjectMetadata},
     *                   if the meta-information does not contain Content-Length,
     *                   transmits the request data in chunked encoding.
     */
    PutObjectResult putObject(String bucketName, String key, InputStream input,
                              ObjectMetadata metadata) throws OSSException, ClientException, IOException;

    /**
     * Delete the specified {@link OSSObject}.
     *
     * @param bucketName Bucket name.
     * @param key        Object key.
     */
    void deleteObject(String bucketName, String key) throws OSSException, ClientException;

    /**
     * Copy an Object that already exists on the OSS into another Object.
     *
     * @param sourceBucketName      the name of the bucket where the source object resides.
     * @param sourceKey             key of source Object.
     * @param destinationBucketName the name of the bucket where the target object is.
     * @param destinationKey        key of the target Object.
     * @return request result {@link CopyObjectResult} instance.
     */
    CopyObjectResult copyObject(String sourceBucketName, String sourceKey,
                                String destinationBucketName, String destinationKey) throws OSSException, ClientException;

    /**
     * Closes the Client instance and frees all the resources that are in use.
     * Once closed, no requests to the OSS will be processed anymore.
     */
    void shutdown();
}