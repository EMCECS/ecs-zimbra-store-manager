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
package com.emc.ecs.zimbra.integration.util;

import com.emc.object.util.ProgressListener;

/**
 * <p>
 * Logs upload progress to ECS while writing into bucket.
 * </p>
 * <p>
 * This ProgressListener is used for debugging purposes only.
 * It works only of logging level is set to <i>DEBUG</i>. So
 * in order to enable this feature you must set:
 * <b>log4j.logger.zimbra.store=DEBUG</b>
 * </p>
 * <p>
 * For more information see
 * <a href="http://wiki.zimbra.com/wiki/Log_Files">
 * Zimbra documentation for Log Files
 * </a>
 * </p>
 */
public class EcsProgressListener implements ProgressListener {

    /* (non-Javadoc)
     * @see com.emc.object.util.ProgressListener#progress(long, long)
     */
    @Override
    public void progress(long completed, long total) {
        EcsLogger.debug("Transfer progress - " + Long.toString(completed) + "/" + Long.toString(total));
    }

    /* (non-Javadoc)
     * @see com.emc.object.util.ProgressListener#transferred(long)
     */
    @Override
    public void transferred(long size) {
        EcsLogger.debug("BytesTransferred - " + Long.toString(size));
    }

}
