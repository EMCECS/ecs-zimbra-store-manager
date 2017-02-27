/*
 * Copyright (c) 2016-2017 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 */
package com.emc.ecs.zimbra.ext.config;

/**
 * <p>
 * Configuration properties of the plugin.
 * </p>
 */
public interface Configuration {

    String getAccessKey();

    String getSecretKey();

    String getEndpoints();

    String getZimbraStoreName();

    Boolean useSmartClient();

    Boolean getCertificateValidationEnabled();

    String getClientProtocol();

    MailboxLocatorScheme getMailboxLocatorScheme();

    String getS3ConfigUri();

    int getNumberOfDeleteThreads();

}
