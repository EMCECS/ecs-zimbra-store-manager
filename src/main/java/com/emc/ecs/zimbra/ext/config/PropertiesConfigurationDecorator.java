/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 */
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

    public static final String ECS_ACCESS_KEY = "ecs.access_key";
    public static final String ECS_SECRET_KEY = "ecs.secret_key";
    public static final String ECS_ENDPOINTS = "ecs.endpoints";
    public static final String ECS_USE_SMART_CLIENT = "ecs.use_smart_client";
    private static final String ECS_CERTIFICATE_VERIFICATION_ENABLED = "ecs.certificate_verification_enabled";
    public static final String ECS_CLIENT_PROTOCOL = "ecs.client_protocol";
    public static final String ECS_S3_CONFIG_URI = "ecs.s3_config_uri";

    private static final String ZIMBRA_STORE_NAME = "zimbra.store_name";
    private static final String MAILBOX_LOCATOR_SCHEME = "zimbra.mailbox_locator_scheme";

    @Override
    public String getAccessKey() {
        return getString(ECS_ACCESS_KEY);
    }

    @Override
    public String getSecretKey() {
        return getString(ECS_SECRET_KEY);
    }

    @Override
    public String getEndpoints() {
        return getString(ECS_ENDPOINTS);
    }

    @Override
    public String getZimbraStoreName() {
        return getNonEmptyString(ZIMBRA_STORE_NAME);
    }

    @Override
    public Boolean useSmartClient() {
        String useSmartClientStr = getString(ECS_USE_SMART_CLIENT);
        return (useSmartClientStr == null) ? null : Boolean.valueOf(useSmartClientStr);
    }

    @Override
    public Boolean getCertificateValidationEnabled() {
        String certificateValidationEnabledStr = getNonEmptyString(ECS_CERTIFICATE_VERIFICATION_ENABLED);
        return Boolean.valueOf(certificateValidationEnabledStr);
    }

    @Override
    public String getClientProtocol() {
        return getString(ECS_CLIENT_PROTOCOL);
    }

    @Override
    public MailboxLocatorScheme getMailboxLocatorScheme() {
        return MailboxLocatorScheme.getValue(getNonEmptyString(MAILBOX_LOCATOR_SCHEME));
    }

    /* (non-Javadoc)
     * @see com.emc.ecs.zimbra.ext.config.Configuration#getS3ConfigUri()
     */
    @Override
    public String getS3ConfigUri() {
        return getString(ECS_S3_CONFIG_URI);
    }

    private String getNonEmptyString(String key) {
        String value = getString(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Missing property: " + key);
        }
        return value;
    }

    private String getString(String key) {
        return getProperties().getProperty(key);
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
