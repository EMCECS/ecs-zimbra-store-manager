/*
 * Copyright (c) 2018 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE.txt
 */
package com.emc.ecs.zimbra.integration;

import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.store.StoreManager;
import com.zimbra.cs.store.external.ExternalMailboxBlob;

import com.emc.ecs.zimbra.integration.util.EcsLogger;

/**
 * <p>
 * ExternalMailboxBlob implementation that optimizes validation
 * </p>
 */

public class EcsMailboxBlob extends ExternalMailboxBlob {

    protected EcsMailboxBlob(Mailbox mbox, int itemId, int revision, String locator) {
        super(mbox, itemId, revision, locator);
    }

    @Override
    public boolean validateBlob() {
        boolean status = false;

        try {
            status = ((EcsStoreManager) StoreManager.getInstance()).validate(getLocator(), getMailbox());
        } catch (Exception e) {
            EcsLogger.warn(String.format("Failed to validate - %s", getLocator()), e);
        }

        return status;
    }

}
