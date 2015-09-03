package com.emc.ecs.zimbra.ext.config;

/**
 * <p>
 * Utility class for getting configuration factory.
 * </p>
 */
public class ConfigurationFactoryUtil {

    private static ConfigurationFactory CONFIGURATION_FACTORY = new DefaultConfigurationFactory();

    public static ConfigurationFactory getConfigurationFactory() {
        return CONFIGURATION_FACTORY;
    }
}
