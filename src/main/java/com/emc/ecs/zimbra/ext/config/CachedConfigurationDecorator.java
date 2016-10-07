/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 *
 * Licensed under the EMC Software License Agreement for Free Software (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * https://github.com/EMCECS/ecs-zimbra-store-manager/blob/master/LICENSE
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.emc.ecs.zimbra.ext.config;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.zimbra.common.util.StringUtil;

/**
 * <p>
 * Configuration decorator that caches requests of the decorated configuration.
 * </p>
 */
public class CachedConfigurationDecorator implements Configuration {

    private Configuration configuration;
    private Map<String, Object> map = new HashMap<>();

    public CachedConfigurationDecorator(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getAccessKey() {
        return (String) getValue("getAccessKey");
    }

    @Override
    public String getSecretKey() {
        return (String) getValue("getSecretKey");
    }

    @Override
    public String getEndpoints() {
        return (String) getValue("getEndpoints");
    }

    @Override
    public String getZimbraStoreName() {
        return (String) getValue("getZimbraServerName");
    }

    @Override
    public Boolean useSmartClient() {
        return (Boolean) getValue("useSmartClient");
    }

    @Override
    public Long getMinimumUploadPartSize() {
        return (Long) getValue("getMinimumUploadPartSize");
    }

    @Override
    public Long getMultipartUploadThreshold() {
        return (Long) getValue("getMultipartUploadThreshold");
    }

    @Override
    public Boolean getCertificateValidationEnabled() {
        return (Boolean) getValue("getCertificateValidationEnabled");
    }

    @Override
    public String getClientProtocol() {
        return (String) getValue("getClientProtocol");
    }

    @Override
    public MailboxLocatorScheme getMailboxLocatorScheme() {
        return (MailboxLocatorScheme) getValue("getMailboxLocatorScheme");
    }

    @Override
    public String getS3ConfigUri() {
        return (String) getValue("getS3ConfigUri");
    }

    private Object getValue(String method) {
        if (map.containsKey(method)) {
            return map.get(method);
        } else {
            try {
                Object value = configuration.getClass().getMethod(method).invoke(configuration);
                map.put(method, value);
                return value;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
