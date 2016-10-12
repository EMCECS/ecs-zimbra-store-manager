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
