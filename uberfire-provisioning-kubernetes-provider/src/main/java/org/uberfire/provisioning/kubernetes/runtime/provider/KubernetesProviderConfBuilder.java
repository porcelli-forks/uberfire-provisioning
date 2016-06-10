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

package org.uberfire.provisioning.kubernetes.runtime.provider;

public class KubernetesProviderConfBuilder {

    private static KubernetesProviderConfBuilder instance;
    private static KubernetesProviderConfiguration config;

    private KubernetesProviderConfBuilder() {
    }

    public static KubernetesProviderConfBuilder newConfig( String providerName ) {
        instance = new KubernetesProviderConfBuilder();
        config = new KubernetesProviderConfiguration( providerName );
        return instance;
    }

    public KubernetesProviderConfBuilder setUsername( String username ) {
        config.setUsername( username );
        return instance;
    }

    public KubernetesProviderConfBuilder setPassword( String password ) {
        config.setPassword( password );
        return instance;
    }

    public KubernetesProviderConfBuilder setMasterUrl( String masterUrl ) {
        config.setMasterUrl( masterUrl );
        return instance;
    }

    public KubernetesProviderConfiguration get() {
        return config;
    }
}
