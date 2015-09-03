package com.emc.ecs.zimbra.integration;

import com.emc.ecs.zimbra.ext.config.Configuration;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactory;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactoryUtil;
import com.zimbra.cs.mailbox.Mailbox;

import java.util.UUID;

/**
 * <p>
 * Helper class that helps to map Zimbra's String locator to
 * com.emc.ecs.zimbra.integration.EcsLocator.
 * </p>
 * <p>
 * Also generates EcsLocator with random UUID used as key
 * in a bucket for Zimbra Blobs.
 * </p>
 */
public class EcsLocatorUtil {

    public static final String ZIMBRA = "zimbra";
    public static final String SLASH = "/";

    public static EcsLocator generateEcsLocator(Mailbox mbox) {
        return new EcsLocator(
                getBucketName(mbox),
                UUID.randomUUID().toString()
        );
    }

    public static String toStringLocator(EcsLocator locator) {
        return String.format("%s/%s", locator.getBucketName(), locator.getKey());
    }

    public static EcsLocator fromStringLocator(String locator) {
        String[] locatorParts = locator.split(SLASH, 2);
        if (locatorParts.length >= 2)
            return new EcsLocator(locatorParts[0], locatorParts[1]);
        else
            throw new IllegalArgumentException("Invalid locator String");
    }

    public static String getBucketName(Mailbox mbox) {
        String serverName = getConfiguration().getZimbraServerName();
        return String.format("%s.%s.%s", ZIMBRA, serverName, mbox.getId());
    }

    private static Configuration getConfiguration() {
        ConfigurationFactoryUtil configurationFactoryUtil = new ConfigurationFactoryUtil();
        ConfigurationFactory configurationFactory = configurationFactoryUtil.getConfigurationFactory();
        return configurationFactory.create();
    }

}
