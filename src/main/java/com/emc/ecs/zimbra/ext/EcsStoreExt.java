/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 */
package com.emc.ecs.zimbra.ext;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.extension.ExtensionException;
import com.zimbra.cs.extension.ZimbraExtension;

/**
 * <p>
 * Implements Zimbra Extension to provide ECS Store Manager functionality
 * to Zimbra Server.
 * </p>
 * <p>
 * EcsStoreExt helps Zimbra Server to locate ECS Store Manager plugin.
 * </p>
 * <p>
 * When Zimbra Server starts up it scans <b>/opt/zimbra/lib/ext/</b>
 * subdirectories for Jar files. If the Jar file contains MANIFEST.MF file
 * containing <i>Zimbra-Extension-Class: com.example.{ExtName}</i>, Zimbra
 * checks whether that class implements com.zimbra.cs.extension.ZimbraExtension,
 * and if the condition passes Jar file is added to Zimbra Server's Class Loader.
 * </p>
 * <p>
 * For more information about implementing Zimbra Extension please see
 * <a href="http://blog.zimbra.com/blog/archives/2010/04/extending-zimbra-with-server-extensions.html">
 * Extending Zimbra with Server Extensions blog entry</a>
 * </p>
 */
public class EcsStoreExt implements ZimbraExtension {

    public static final String EXTENSION_NAME = "ecs-store-manager";

    @Override
    public String getName() {
        return EXTENSION_NAME;
    }

    @Override
    public void init() throws ExtensionException, ServiceException {
        ZimbraLog.store.debug("ECS: initializing ECS Store Manager Extension");
    }

    @Override
    public void destroy() {
        ZimbraLog.store.debug("ECS: destroying ECS Store Manager Extension");
    }
}
