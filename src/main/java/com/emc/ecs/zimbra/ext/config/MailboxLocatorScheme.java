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

/**
 * @author seibed
 *
 */
public enum MailboxLocatorScheme {

    Bucket("Bucket"), Prefix("Prefix");

    MailboxLocatorScheme(String propertyValue) {
        _propertyValue = propertyValue;
    }

    private final String _propertyValue;

    public static MailboxLocatorScheme getValue(String propertyValue) {
        for (MailboxLocatorScheme scheme : values()) {
            if (scheme._propertyValue.equals(propertyValue)) {
                return scheme;
            }
        }
        return Prefix;
    }

}
