/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 */
package com.emc.ecs.zimbra.integration;

import com.emc.ecs.zimbra.ext.config.Configuration;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactory;
import com.emc.ecs.zimbra.ext.config.ConfigurationFactoryUtil;
import com.emc.ecs.zimbra.ext.config.MailboxLocatorScheme;
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
                getNewKey(mbox)
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

    public static String getBucketNameBase() {
        return String.format("%s.%s", ZIMBRA, getConfiguration().getZimbraStoreName());
    }

    public static String getBucketName(Mailbox mbox) {
        switch (getConfiguration().getMailboxLocatorScheme()) {
        case Bucket:
            return String.format("%s.%s", getBucketNameBase(), mbox.getId());
        case Prefix:
        default:
            return getBucketNameBase();
        }
    }

    public static String getNewKey(Mailbox mbox) {
        return String.format("%s%s", getPrefix(mbox), UUID.randomUUID().toString());
    }

    private static Configuration getConfiguration() {
        ConfigurationFactory configurationFactory = ConfigurationFactoryUtil.getConfigurationFactory();
        return configurationFactory.create();
    }

    /**
     * @param mbox
     * @return
     */
    public static String getPrefix(Mailbox mbox) {
        switch (getConfiguration().getMailboxLocatorScheme()) {
        case Bucket:
            return "";
        case Prefix:
        default:
            return String.format("%s.", mbox.getId());
        }
    }

    /**
     * @return <code>true</code> if the scheme uses one bucket for all mailboxes, <code>false</code> otherwise.
     */
    public static boolean useSingleBucket() {
        return MailboxLocatorScheme.Bucket != getConfiguration().getMailboxLocatorScheme();
    }

}
