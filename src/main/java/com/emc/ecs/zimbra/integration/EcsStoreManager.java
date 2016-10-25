/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 */
package com.emc.ecs.zimbra.integration;

import com.emc.ecs.zimbra.integration.util.EcsLogger;
import com.emc.object.s3.S3ObjectMetadata;
import com.emc.object.s3.bean.Bucket;
import com.emc.object.s3.bean.ListObjectsResult;
import com.emc.object.s3.bean.S3Object;
import com.emc.object.s3.jersey.S3JerseyClient;
import com.emc.object.s3.request.ListObjectsRequest;
import com.emc.object.s3.request.PutObjectRequest;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.store.external.ExternalStoreManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * ECS Store Manager that implements
 * <a href="http://wiki.zimbra.com/wiki/StoreManagerSDK">Zimbra Server's StoreManager SDK</a>.
 * </p>
 * <p>
 * This class is used to write, read and delete blobs from EMC ECS store using ECS S3 client.
 * </p>
 * <p>
 * Uses {@link com.emc.object.s3.jersey.S3JerseyClient} for uploading, reading and deleting blobs.<br/>
 * </p>
 */

public class EcsStoreManager extends ExternalStoreManager {

    private S3JerseyClient client;

    private final Set<String> bucketNames = new HashSet<String>();

    /**
     * <p>
     * This method is invoked during initialization of Zimbra Server's internal services.
     * Initializes {@link com.emc.object.s3.jersey.S3JerseyClient}.
     * </p>
     *
     * @throws IOException      see {@link com.zimbra.cs.store.external.ExternalStoreManager#startup()}
     * @throws ServiceException see {@link com.zimbra.cs.store.external.ExternalStoreManager#startup()}
     */
    @Override
    public void startup() throws IOException, ServiceException {
        EcsLogger.error("Starting up ECS Store Manager");
        super.startup();
        try {
            client = S3ClientFactory.getS3Client();
            fillBucketNames();
        } catch (Exception e) {
            client = null;
            EcsLogger.error(e.getMessage());
            throw ServiceException.RESOURCE_UNREACHABLE(e.getMessage(), e, (ServiceException.Argument) null);
        }
    }

    /**
     */
    private void fillBucketNames() {
        String bucketNameBase = EcsLocatorUtil.getBucketNameBase();
        bucketNames.clear();
        for (Bucket bucket : client.listBuckets().getBuckets()) {
            if (bucket.getName().startsWith(bucketNameBase)) {
                bucketNames.add(bucket.getName());
            }
        }
        if (EcsLocatorUtil.useSingleBucket()) {
            createBucketAsNeeded(EcsLocatorUtil.getBucketName(null));
        }
    }

    /**
     * <p>
     * This method is invoked during Zimbra Server's shutdown process.
     * Stops {@link com.emc.object.s3.jersey.S3JerseyClient} and releases its
     * resources.
     * </p>
     */
    @SuppressWarnings("deprecation")
    @Override
    public void shutdown() {
        EcsLogger.debug("Shutting down ECS Store Manager");
        super.shutdown();
        client.shutdown();
        bucketNames.clear();
    }

    /**
     * <p>
     * Writes input stream to ECS store. Uses Mailbox ID
     * and zimbra.store_name property to generate bucket name.
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

        if (!EcsLocatorUtil.useSingleBucket()) {
            createBucketAsNeeded(locator.getBucketName());
        }

        S3ObjectMetadata metadata = new S3ObjectMetadata();
        if (actualSize > 0) {
            metadata.setContentLength(actualSize);
        }

        PutObjectRequest request = new PutObjectRequest(locator.getBucketName(), locator.getKey(), in);
        request.setObjectMetadata(metadata);
        client.putObject(request);

        String stringLocator = EcsLocatorUtil.toStringLocator(locator);
        EcsLogger.debug(String.format("writeStreamToStore() - end: locator - %s", stringLocator));
        return stringLocator;
    }

    /**
     * @param bucketName
     */
    private void createBucketAsNeeded(String bucketName) {
        if (!bucketNames.contains(bucketName)) {
            client.createBucket(bucketName);
            bucketNames.add(bucketName);
        }
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
        return client.getObject(el.getBucketName(), el.getKey()).getObject();
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
            client.deleteObject(el.getBucketName(), el.getKey());
        } catch (Exception e) {
            EcsLogger.error(String.format("Failed to delete from - %s", locator), e);
            return false;
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
        List<String> result = new ArrayList<>();
        if (bucketNames.contains(bucketName)) {
            ListObjectsRequest request = new ListObjectsRequest(bucketName);
            String prefix = EcsLocatorUtil.getPrefix(mbox);
            if ((prefix != null) && (prefix.length() > 0)) {
                request.setPrefix(prefix);
            }
            boolean listingIncomplete = true;

            while(listingIncomplete) {
                ListObjectsResult objectListing = client.listObjects(request);
        
                for (S3Object object : objectListing.getObjects()) {
                    EcsLocator ecsLocator = new EcsLocator(bucketName, object.getKey());
                    result.add(EcsLocatorUtil.toStringLocator(ecsLocator));
                }

                if (objectListing.isTruncated()) {
                    request.setMarker(objectListing.getNextMarker());
                } else {
                    listingIncomplete = false;
                }
            }
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
