/**
 * Copyright 2016-2017 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.emc.ecs.zimbra.ext.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author seibed
 *
 */
public class ConfigurationClassesTest extends Assert {

    @Test
    public void testAllClasses() {
        Configuration configuration = ConfigurationFactoryUtil.getConfigurationFactory().create();
        configuration.getAccessKey();
        configuration.getSecretKey();
        assertEquals("", configuration.getS3ConfigUri());
        assertEquals("https", configuration.getClientProtocol());
        assertEquals("https://object.ecstestdrive.com:443", configuration.getEndpoints());
        assertEquals("store1", configuration.getZimbraStoreName());
        assertEquals(MailboxLocatorScheme.Prefix, configuration.getMailboxLocatorScheme());
        assertEquals(Boolean.FALSE, configuration.useSmartClient());
        assertEquals(Boolean.TRUE, configuration.getCertificateValidationEnabled());
        assertEquals(25, configuration.getNumberOfDeleteThreads());
    }

}
