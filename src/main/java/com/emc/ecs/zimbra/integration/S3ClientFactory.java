/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 *
 * or in the "LICENSE.txt" file accompanying this file.
 */
package com.emc.ecs.zimbra.integration;

import com.emc.ecs.zimbra.ext.config.Configuration;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactory;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactoryUtil;
import com.emc.ecs.zimbra.ext.config.PropertiesConfigurationDecorator;
//import com.emc.ecs.zimbra.integration.util.EcsLogger;
import com.emc.object.Protocol;
import com.emc.object.s3.S3Config;
import com.emc.object.s3.jersey.S3JerseyClient;
import com.emc.object.util.ConfigUri;
import com.emc.rest.smart.ecs.Vdc;
import com.zimbra.common.util.StringUtil;

//import static com.amazonaws.SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Factory class used to create {@link com.emc.object.s3.jersey.S3JerseyClient} instances.
 * </p>
 */
public class S3ClientFactory {

    public static S3JerseyClient getS3Client() throws Exception {
        Configuration configuration = getConfiguration();

        // TODO: restore this behavior
//        if (configuration.getCertificateValidationEnabled() == true) {
//            System.clearProperty(DISABLE_CERT_CHECKING_SYSTEM_PROPERTY);
//        } else {
//            EcsLogger.warn("SSL certificate validation is disabled. SSL certificate validation must be enabled on production environment");
//            System.setProperty(DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
//        }

        S3Config s3Config;
        if (configuration.getS3ConfigUri() != null) {
            ConfigUri<S3Config> s3Uri = new ConfigUri<S3Config>(S3Config.class);
            s3Config = s3Uri.parseUri(configuration.getS3ConfigUri());
        } else {
            checkProperties(configuration);
            if (configuration.useSmartClient() == true) {
                s3Config = new S3Config(getClientProtocol(), getVdcs(configuration.getEndpoints()));
            } else {
                s3Config = new S3Config(new URI(getFirstEndpoint(configuration.getEndpoints())));
            }
            s3Config.setIdentity(configuration.getAccessKey());
            s3Config.setSecretKey(configuration.getSecretKey());
        }

        return new S3JerseyClient(s3Config);
    }

    /**
     * @param configuration
     */
    private static void checkProperties(Configuration configuration) {
        List<String> missingProperties = new ArrayList<String>(5);
        if (configuration.useSmartClient() == null) {
            missingProperties.add(PropertiesConfigurationDecorator.ECS_USE_SMART_CLIENT);
        }
        if (isNullOrEmpty(configuration.getAccessKey())) {
            missingProperties.add(PropertiesConfigurationDecorator.ECS_ACCESS_KEY);
        }
        if (isNullOrEmpty(configuration.getClientProtocol())) {
            missingProperties.add(PropertiesConfigurationDecorator.ECS_CLIENT_PROTOCOL);
        }
        if (isNullOrEmpty(configuration.getEndpoints())) {
            missingProperties.add(PropertiesConfigurationDecorator.ECS_ENDPOINTS);
        }
        if (isNullOrEmpty(configuration.getSecretKey())) {
            missingProperties.add(PropertiesConfigurationDecorator.ECS_SECRET_KEY);
        }
        if (missingProperties.size() > 0) {
            String errorMessage = "You must set a value for either the property " + PropertiesConfigurationDecorator.ECS_S3_CONFIG_URI + ", or for ";
            for (int i = 0; i < missingProperties.size(); ++i) {
                errorMessage = errorMessage + missingProperties.get(i);
                if (i < missingProperties.size() - 2) {
                    errorMessage = errorMessage + ", ";
                } else if (i == missingProperties.size() - 2) {
                    errorMessage = errorMessage + " and ";
                } else if (i == missingProperties.size() - 1) {
                    errorMessage = errorMessage + ".";
                }
            }
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * @param string
     * @return
     */
    private static boolean isNullOrEmpty(String string) {
        return StringUtil.isNullOrEmpty(string);
    }

    /**
     * @return
     */
    private static Vdc[] getVdcs(String endpoints) {
        String[] uriStrings = getUriStrings(endpoints);
        Vdc[] vdcs = null;
        if ((uriStrings != null) && (uriStrings.length > 0)) {
            vdcs = new Vdc[uriStrings.length];
            for (int i = 0; i < uriStrings.length; ++i) {
                vdcs[i] = new Vdc(uriStrings[i]);
            }
        }
        return vdcs;
    }

    private static Configuration getConfiguration() {
        ConfigurationFactory configurationFactory = ConfigurationFactoryUtil.getConfigurationFactory();
        return configurationFactory.create();
    }

    private static String getFirstEndpoint(String s3EndpointsString) {
        String[] uris = getUriStrings(s3EndpointsString);
        if (uris.length > 0)
            return uris[0];
        else
            return null;
    }

    /**
     * @param s3EndpointsString
     * @return
     */
    private static String[] getUriStrings(String s3EndpointsString) {
        return s3EndpointsString.split(",");
    }

    private static Protocol getClientProtocol() {
        Configuration conf = getConfiguration();
        try {
            return Protocol.valueOf(conf.getClientProtocol());
        } catch (IllegalArgumentException e) {
            return Protocol.HTTP;
        }
    }

}
