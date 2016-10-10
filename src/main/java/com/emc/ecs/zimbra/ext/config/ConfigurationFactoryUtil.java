/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
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
