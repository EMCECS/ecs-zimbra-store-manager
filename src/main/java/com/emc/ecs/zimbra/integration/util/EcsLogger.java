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

import com.zimbra.common.util.ZimbraLog;

/**
 * <p>
 * Wrapper for com.zimbra.common.util.ZimbraLog. Adds <i>"ECS: "</i> prefix
 * to logs in order to make reading logs provided by this plugin easy.
 * </p>
 * <p>
 * Logs are written to Zimbra Server's store logs (by default: /opt/zimbra/log/mailbox.log).
 * </p>
 * <p>
 * For more information see
 * <a href="http://wiki.zimbra.com/wiki/Log_Files">
 * Zimbra documentation for Log Files
 * </a>
 * </p>
 */
public class EcsLogger {

    public static final String ECS_PREFIX = "ECS: ";

    public static void trace(String message) {
        ZimbraLog.store.trace(ECS_PREFIX + message);
    }

    public static void trace(String message, Throwable e) {
        ZimbraLog.store.trace(ECS_PREFIX + message, e);
    }

    public static void debug(String message) {
        ZimbraLog.store.debug(ECS_PREFIX + message);
    }

    public static void debug(String message, Throwable e) {
        ZimbraLog.store.debug(ECS_PREFIX + message, e);
    }

    public static void info(String message) {
        ZimbraLog.store.info(ECS_PREFIX + message);
    }

    public static void info(String message, Throwable e) {
        ZimbraLog.store.info(ECS_PREFIX + message, e);
    }

    public static void warn(String message) {
        ZimbraLog.store.warn(ECS_PREFIX + message);
    }

    public static void warn(String message, Throwable e) {
        ZimbraLog.store.warn(ECS_PREFIX + message, e);
    }

    public static void error(String message) {
        ZimbraLog.store.error(message);
    }

    public static void error(String message, Throwable e) {
        ZimbraLog.store.error(message, e);
    }

    public static void fatal(String message) {
        ZimbraLog.store.fatal(message);
    }

    public static void fatal(String message, Throwable e) {
        ZimbraLog.store.fatal(message, e);
    }

}
