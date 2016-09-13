package com.emc.ecs.zimbra.integration;

import com.emc.ecs.zimbra.ext.config.Configuration;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactory;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactoryUtil;
//import com.emc.ecs.zimbra.integration.util.EcsLogger;
import com.emc.object.Protocol;
import com.emc.object.s3.S3Config;
import com.emc.object.s3.jersey.S3JerseyClient;
import com.emc.rest.smart.ecs.Vdc;

//import static com.amazonaws.SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY;

import java.net.URI;

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
        if (configuration.useSmartClient() == true) {
            s3Config = new S3Config(getClientProtocol(), getVdcs(configuration.getEndpoints()));
        } else {
            s3Config = new S3Config(new URI(getFirstEndpoint(configuration.getEndpoints())));
        }
        s3Config.setIdentity(configuration.getAccessKey());
        s3Config.setSecretKey(configuration.getSecretKey());

        return new S3JerseyClient(s3Config);
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

    /**
     * @return
     */
    public static long getMultipartUploadThreshold() {
        return getConfiguration().getMultipartUploadThreshold();
    }

}
