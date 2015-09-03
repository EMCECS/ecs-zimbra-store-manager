package com.emc.ecs.zimbra.integration;

/**
 * <p>
 * Locator that represents location of the Zimbra's Blob in ECS
 * </p>
 */
public class EcsLocator {

    private String bucketName;
    private String key;

    public EcsLocator(String bucketName, String key) {
        this.bucketName = bucketName;
        this.key = key;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
