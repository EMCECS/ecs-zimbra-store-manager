package com.emc.ecs.zimbra.integration;

import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.emc.ecs.zimbra.ext.config.Configuration;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactory;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactoryUtil;
import com.emc.ecs.zimbra.integration.util.EcsLogger;
import com.emc.vipr.services.s3.ViPRS3Client;
import com.emc.vipr.services.s3.ViPRS3Config;

import static com.amazonaws.SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY;

/**
 * <p>
 * Factory class used to create {@link com.emc.vipr.services.s3.ViPRS3Client} and
 * {@link com.amazonaws.services.s3.transfer.TransferManager} instances.
 * </p>
 */
public class S3ClientFactory {

    public static ViPRS3Client getS3Client() {
        Configuration configuration = getConfiguration();

        BasicAWSCredentials creds = new BasicAWSCredentials(configuration.getAccessKey(), configuration.getSecretKey());

        ViPRS3Client client = null;

        if (configuration.getCertificateValidationEnabled() == true) {
            System.clearProperty(DISABLE_CERT_CHECKING_SYSTEM_PROPERTY);
        } else {
            EcsLogger.warn("SSL certificate validation is disabled. SSL certificate validation must be enabled on production environment");
            System.setProperty(DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        }

        if (configuration.useSmartClient() == true) {
            ViPRS3Config viprConfig =
                    new ViPRS3Config().withS3Endpoints(configuration.getEndpoints());
            viprConfig.setCredentialsProvider(new StaticCredentialsProvider(creds));
            viprConfig.setProtocol(getClientProtocol());
            client = new ViPRS3Client(viprConfig);
        } else {
            client = new ViPRS3Client(getFirstEndpoint(configuration.getEndpoints()), creds);
        }

        return client;
    }

    public static TransferManager getTransferManager() {
        TransferManager transferManager = new TransferManager(getS3Client());
        Configuration configuration = getConfiguration();

        TransferManagerConfiguration tmc = new TransferManagerConfiguration();
        tmc.setMinimumUploadPartSize(configuration.getMinimumUploadPartSize());
        tmc.setMultipartUploadThreshold(configuration.getMultipartUploadThreshold());
        transferManager.setConfiguration(tmc);

        return transferManager;
    }

    private static Configuration getConfiguration() {
        ConfigurationFactoryUtil configurationFactoryUtil = new ConfigurationFactoryUtil();
        ConfigurationFactory configurationFactory = configurationFactoryUtil.getConfigurationFactory();
        return configurationFactory.create();
    }

    private static String getFirstEndpoint(String s3EndpointsString) {
        String[] uris = s3EndpointsString.split(",");
        if (uris.length > 0)
            return uris[0];
        else
            return null;
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
