package com.emc.ecs.zimbra.ext.config;

/**
 * <p>
 * Default implementation of Configuration Factory.
 * </p>
 */
public class DefaultConfigurationFactory implements ConfigurationFactory {

    private static final Configuration CONFIGURATION = new CachedConfigurationDecorator(new PropertiesConfigurationDecorator());

    /**
     * * <p>
     * Creates Property based configuration wrapped with Caching configuration decorator.
     * </p>
     *
     * @return Configuration implementation
     */
    @Override
    public Configuration create() {
        return CONFIGURATION;
    }

}
