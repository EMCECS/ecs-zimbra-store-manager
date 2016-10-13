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
package com.emc.ecs.zimbra.ext.config;

/**
 * <p>
 * Utility class for getting configuration factory.
 * </p>
 */
public class ConfigurationFactoryUtil {

    /**
     * Singleton, shouldn't be instantiated.
     */
    private ConfigurationFactoryUtil() {}

    private static ConfigurationFactory CONFIGURATION_FACTORY = new DefaultConfigurationFactory();

    public static ConfigurationFactory getConfigurationFactory() {
        return CONFIGURATION_FACTORY;
    }
}
