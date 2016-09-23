package com.emc.ecs.zimbra.ext.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * Configuration that reads properties from <i>ecs.properties</i>
 * </p>
 */
public class PropertiesConfigurationDecorator implements Configuration {

    private static final String ECS_PROPERTIES_FILE = "ecs.properties";

    private static final String ECS_ACCESS_KEY = "ecs.access_key";
    private static final String ECS_SECRET_KEY = "ecs.secret_key";
    private static final String ECS_ENDPOINTS = "ecs.endpoints";
    private static final String ECS_USER_SMART_CLIENT = "ecs.use_smart_client";
    private static final String ECS_MINIMUM_UPLOAD_PART_SIZE = "ecs.minimum_upload_part_size";
    private static final String ECS_MULTIPART_UPLOAD_THRESHOLD = "ecs.multipart_upload_threshold";
    private static final String ECS_CERTIFICATE_VERIFICATION_ENABLED = "ecs.certificate_verification_enabled";
    private static final String ECS_CLIENT_PROTOCOL = "ecs.client_protocol";

    private static final String ZIMBRA_STORE_NAME = "zimbra.store_name";
    private static final String MAILBOX_LOCATOR_SCHEME = "zimbra.mailbox_locator_scheme";

    @Override
    public String getAccessKey() {
        return getNonEmptyString(ECS_ACCESS_KEY);
    }

    @Override
    public String getSecretKey() {
        return getNonEmptyString(ECS_SECRET_KEY);
    }

    @Override
    public String getEndpoints() {
        return getNonEmptyString(ECS_ENDPOINTS);
    }

    @Override
    public String getZimbraStoreName() {
        return getNonEmptyString(ZIMBRA_STORE_NAME);
    }

    @Override
    public Boolean useSmartClient() {
        String useSmartClientStr = getNonEmptyString(ECS_USER_SMART_CLIENT);
        return Boolean.valueOf(useSmartClientStr);
    }

    @Override
    public Long getMinimumUploadPartSize() {
        return Long.valueOf(getNonEmptyString(ECS_MINIMUM_UPLOAD_PART_SIZE));
    }

    @Override
    public Long getMultipartUploadThreshold() {
        return Long.valueOf(getNonEmptyString(ECS_MULTIPART_UPLOAD_THRESHOLD));
    }

    @Override
    public Boolean getCertificateValidationEnabled() {
        String certificateValidationEnabledStr = getNonEmptyString(ECS_CERTIFICATE_VERIFICATION_ENABLED);
        return Boolean.valueOf(certificateValidationEnabledStr);
    }

    @Override
    public String getClientProtocol() {
        return getNonEmptyString(ECS_CLIENT_PROTOCOL);
    }

    @Override
    public MailboxLocatorScheme getMailboxLocatorScheme() {
        return MailboxLocatorScheme.getValue(getNonEmptyString(MAILBOX_LOCATOR_SCHEME));
    }

    private String getNonEmptyString(String key) {
        String value = getProperties().getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException();
        }
        return value;
    }

    private Properties getProperties() {
        Properties props = null;
        try {
            InputStream in = PropertiesConfigurationDecorator.class.getClassLoader().getResourceAsStream(ECS_PROPERTIES_FILE);
            props = new Properties();
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }

}
