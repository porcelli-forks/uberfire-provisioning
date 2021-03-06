/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.provisioning.runtime.providers.base;

import org.uberfire.provisioning.runtime.providers.ProviderType;

/**
 * @author salaboy
 */
public abstract class BaseProviderType implements ProviderType {

    private final String providerName;
    private final String version;
    private final Class provider;

    public BaseProviderType(String providerName,
            String version,
            Class provider) {
        this.providerName = providerName;
        this.version = version;
        this.provider = provider;
    }

    @Override
    public String getProviderTypeName() {
        return providerName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Class getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "BaseProviderType{" + "providerName=" + providerName + ", version=" + version + ", providerClass=" + provider + '}';
    }

}
