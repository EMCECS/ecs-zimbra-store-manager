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
package com.emc.ecs.zimbra.integration;

/**
 * <p>
 * Locator that represents location of the Zimbra's Blob in ECS
 * </p>
 */
public class EcsLocator {

    private String bucketName;
    private String key;

    public EcsLocator(String bucketName, String key) {
        this.bucketName = bucketName;
        this.key = key;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
