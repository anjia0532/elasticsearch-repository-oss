package org.elasticsearch.aliyun.oss.service;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.repository.oss.OssRepository;

import java.io.InputStream;

/**
 * OSS Service implementation for creating oss client
 * Created by yangkongshi on 2017/11/24.
 */
public class OssServiceImpl extends AbstractComponent implements OssService {
    private OSS client;

    public OssServiceImpl(Settings settings, RepositoryMetaData metadata) {
        super(settings);
        this.client = createClient(metadata);
    }

    /**
     * @param repositoryMetaData metadata for the repository including name and settings
     * @return oss client
     */
    private OSS createClient(RepositoryMetaData repositoryMetaData) {
        OSS client;
        String accessKeyId =
                OssRepository.getSetting(OssClientSettings.ACCESS_KEY_ID, repositoryMetaData);
        String secretAccessKey =
                OssRepository.getSetting(OssClientSettings.SECRET_ACCESS_KEY, repositoryMetaData);
        String endpoint = OssRepository.getSetting(OssClientSettings.ENDPOINT, repositoryMetaData);

        // Using the specified OSS Endpoint, temporary Token information provided by the STS
        String securityToken = OssClientSettings.SECURITY_TOKEN.get(repositoryMetaData.settings());
        if (Strings.hasLength(securityToken)) {
            client = new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey, securityToken);
        } else {
            client = new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
        }
        return client;
    }

    @Override public DeleteObjectsResult deleteObjects(DeleteObjectsRequest deleteObjectsRequest)
            throws OSSException, ClientException {
        return this.client.deleteObjects(deleteObjectsRequest);
    }


    @Override public boolean doesObjectExist(String bucketName, String key)
            throws OSSException, ClientException {
        return this.client.doesObjectExist(bucketName, key);
    }

    @Override public boolean doesBucketExist(String bucketName)
            throws OSSException, ClientException {
        return this.client.doesBucketExist(bucketName);
    }


    @Override public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
            throws OSSException, ClientException {
        return this.client.listObjects(listObjectsRequest);
    }


    @Override public OSSObject getObject(String bucketName, String key)
            throws OSSException, ClientException {
        restoreObject(bucketName, key);
        return this.client.getObject(bucketName, key);
    }

    private void restoreObject(String bucketName, String key){
        ObjectMetadata objectMetadata = this.client.getObjectMetadata(bucketName, key);
        StorageClass storageClass = objectMetadata.getObjectStorageClass();
        if (storageClass == StorageClass.Archive) {
            this.client.restoreObject(bucketName, key);
            try {
                do {
                    Thread.sleep(100);
                    objectMetadata = this.client.getObjectMetadata(bucketName, key);
                    logger.debug("restore object:{}",objectMetadata);
                } while (!objectMetadata.isRestoreCompleted());
            } catch (InterruptedException e) {
                logger.error("can not restore object {}/{}", bucketName,key,e);
            }
        }
    }
    @Override public PutObjectResult putObject(String bucketName, String key, InputStream input,
                                               ObjectMetadata metadata) throws OSSException, ClientException {
        return this.client.putObject(bucketName, key, input, metadata);
    }

    @Override public void deleteObject(String bucketName, String key)
            throws OSSException, ClientException {
        this.client.deleteObject(bucketName, key);
    }

    @Override public CopyObjectResult copyObject(String sourceBucketName, String sourceKey,
                                                 String destinationBucketName, String destinationKey) throws OSSException, ClientException {
        return this.client
                .copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
    }

    @Override public void shutdown() {
        this.client.shutdown();
    }
}