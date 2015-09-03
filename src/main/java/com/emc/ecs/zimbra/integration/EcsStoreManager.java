package com.emc.ecs.zimbra.integration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.emc.ecs.zimbra.integration.util.EcsLogger;
import com.emc.ecs.zimbra.integration.util.EcsProgressListener;
import com.emc.vipr.services.s3.ViPRS3Client;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.store.external.ExternalStoreManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * ECS Store Manager that implements
 * <a href="http://wiki.zimbra.com/wiki/StoreManagerSDK">Zimbra Server's StoreManager SDK</a>.
 * </p>
 * <p>
 * This class is used to write, read and delete blobs from EMC ECS store using ViPR S3 client.
 * </p>
 * <p>
 * Uses {@link com.emc.vipr.services.s3.ViPRS3Client} for reading and deleting blobs.<br/>
 * Uses {@link com.amazonaws.services.s3.transfer.TransferManager} for uploading blobs
 * </p>
 */

public class EcsStoreManager extends ExternalStoreManager {

    public static final String BUCKET_NOT_EMPTY_ERROR_CODE = "BucketNotEmpty";
    private ViPRS3Client viprClient;
    private TransferManager transferManager;

    /**
     * <p>
     * This method is invoked during initialization of Zimbra Server's internal services.
     * Initializes {@link com.emc.vipr.services.s3.ViPRS3Client} and
     * {@link com.amazonaws.services.s3.transfer.TransferManager}.
     * </p>
     *
     * @throws IOException      see {@link com.zimbra.cs.store.external.ExternalStoreManager#startup()}
     * @throws ServiceException see {@link com.zimbra.cs.store.external.ExternalStoreManager#startup()}
     */
    @Override
    public void startup() throws IOException, ServiceException {
        EcsLogger.debug("Starting up ECS Store Manager");
        super.startup();
        viprClient = S3ClientFactory.getS3Client();
        transferManager = S3ClientFactory.getTransferManager();
    }

    /**
     * <p>
     * This method is invoked during Zimbra Server's shutdown process.
     * Stops {@link com.emc.vipr.services.s3.ViPRS3Client} and
     * {@link com.amazonaws.services.s3.transfer.TransferManager} and releases their
     * resources.
     * </p>
     */
    @Override
    public void shutdown() {
        EcsLogger.debug("Shutting down ECS Store Manager");
        super.shutdown();
        transferManager.shutdownNow();
        viprClient.shutdown();
    }

    /**
     * <p>
     * Writes input stream to ECS store. Uses Mailbox ID
     * and zimbra.server_name property to generate bucket name.
     * Random UUID is used as a key for the blob inside the bucket.
     * </p>
     *
     * @param in         InputStream containing data to be written
     * @param actualSize Size of data in stream, or -1 if size is unknown. Used to determine
     *                   need to use Multipart upload.
     * @param mbox       Mailbox which contains the blob.
     * @return locator string for the stored blob, unique identifier created by storage protocol
     * @throws IOException
     */
    @Override
    public String writeStreamToStore(InputStream in, long actualSize, Mailbox mbox) throws IOException {
        EcsLogger.debug(String.format("writeStreamToStore() - start: actualSize - %s, accountId - %s", actualSize, mbox.getId()));

        EcsLocator locator = EcsLocatorUtil.generateEcsLocator(mbox);

        if (!viprClient.doesBucketExist(locator.getBucketName())) {
            viprClient.createBucket(locator.getBucketName());
        }

        ObjectMetadata metadata = new ObjectMetadata();
        if (actualSize > 0) {
            metadata.setContentLength(actualSize);
        }
        Upload upload = transferManager.upload(locator.getBucketName(), locator.getKey(), in, metadata);
        upload.addProgressListener(new EcsProgressListener());
        try {
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            EcsLogger.error(String.format("Failed to wait for upload completion"), e);
        }

        String stringLocator = EcsLocatorUtil.toStringLocator(locator);
        EcsLogger.debug(String.format("writeStreamToStore() - end: locator - %s", stringLocator));
        return stringLocator;
    }

    /**
     * <p>
     * Creates an input stream for reading data from ECS Store.
     * After reading is finished Zimbra immediately closes the returned stream.
     * </p>
     *
     * @param locator identifier string for the blob as returned from write operation
     * @param mbox    Mailbox which contains the blob
     * @return InputStream containing the data
     * @throws IOException
     */
    @Override
    public InputStream readStreamFromStore(String locator, Mailbox mbox) throws IOException {
        EcsLogger.debug(String.format("readStreamFromStore() - start: locator - %s, accountId - %s", locator, mbox.getId()));

        EcsLocator el = EcsLocatorUtil.fromStringLocator(locator);

        EcsLogger.debug(String.format("readStreamFromStore() - reading: bucket - %s, key - %s", el.getBucketName(), el.getKey()));
        S3Object object = viprClient.getObject(el.getBucketName(), el.getKey());
        return object.getObjectContent();
    }

    /**
     * <p>
     * Delete a blob from the store
     * </p>
     *
     * @param locator identifier string for the blob
     * @param mbox    Mailbox which contains the blob
     * @return true on success false on failure
     * @throws IOException
     */
    @Override
    public boolean deleteFromStore(String locator, Mailbox mbox) throws IOException {
        EcsLogger.debug(String.format("deleteFromStore() - start: locator - %s, accountId - %s", locator, mbox.getId()));

        EcsLocator el = EcsLocatorUtil.fromStringLocator(locator);

        EcsLogger.debug(String.format("deleteFromStore() - deleting: bucket - %s, key - %s", el.getBucketName(), el.getKey()));
        try {
            viprClient.deleteObject(el.getBucketName(), el.getKey());
        } catch (AmazonClientException e) {
            EcsLogger.error(String.format("Failed to delete from - %s", locator), e);
            return false;
        }

        try {
            viprClient.deleteBucket(el.getBucketName());
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 409 && e.getErrorCode().equals(BUCKET_NOT_EMPTY_ERROR_CODE)) {
                // if the bucket was not empty then receiving this error is a correct behaviour
                // suppressing this exception helps to avoid checking whether the bucket is empty
            } else {
                throw e;
            }
        }

        return true;
    }

    /**
     * <p>
     * Returns list of blob locators related to Mailbox. Helps
     * Zimbra to run consistency checks.
     * </p>
     *
     * @param mbox Mailbox which contains the blob
     * @return List of all blob locators related to input Mailbox
     * @throws IOException
     */
    @Override
    public List<String> getAllBlobPaths(Mailbox mbox) throws IOException {
        String bucketName = EcsLocatorUtil.getBucketName(mbox);
        ObjectListing objectListing = viprClient.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName));

        List<String> result = new ArrayList<>();
        for (S3ObjectSummary sum : objectListing.getObjectSummaries()) {
            EcsLocator ecsLocator = new EcsLocator(bucketName, sum.getKey());
            result.add(EcsLocatorUtil.toStringLocator(ecsLocator));
        }

        return result;
    }

    /**
     * <p>
     * Returns true if the given feature is supported.
     * </p>
     * <p>
     * Enables Centralized store feature.
     * </p>
     *
     * @param feature feature to be checked for support
     * @return true if given feature is supported
     */
    @Override
    public boolean supports(StoreFeature feature) {
        if (feature == StoreFeature.CENTRALIZED) {
            return true;
        } else {
            return super.supports(feature);
        }
    }

}
