package com.emc.ecs.zimbra.ext.config;

/**
 * <p>
 * Configuration properties of the plugin.
 * </p>
 */
public interface Configuration {

    String getAccessKey();

    String getSecretKey();

    String getEndpoints();

    String getZimbraStoreName();

    Boolean useSmartClient();

    Long getMinimumUploadPartSize();

    Long getMultipartUploadThreshold();

    Boolean getCertificateValidationEnabled();

    String getClientProtocol();

    MailboxLocatorScheme getMailboxLocatorScheme();

}
