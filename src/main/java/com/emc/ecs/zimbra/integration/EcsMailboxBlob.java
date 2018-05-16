package com.emc.ecs.zimbra.integration;

import java.io.IOException;

import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.store.StoreManager;
import com.zimbra.cs.store.external.ExternalMailboxBlob;

import com.emc.ecs.zimbra.integration.util.EcsLogger;

/**
 * <p>
 * ExternalMailboxBlob implementation that supports validation
 * </p>
 */

public class EcsMailboxBlob extends ExternalMailboxBlob {

    protected EcsMailboxBlob(Mailbox mbox, int itemId, int revision, String locator) {
        super(mbox, itemId, revision, locator);
    }

    @Override
    public boolean validateBlob() {
        EcsStoreManager sm = (EcsStoreManager) StoreManager.getInstance();
        boolean status = false;

        try {
            status = sm.validateFromStore(getLocator(), getMailbox());
        } catch (IOException e) {
            EcsLogger.warn(String.format("Failed to validate - %s", getLocator()));
        }

        return status;
    }

}
