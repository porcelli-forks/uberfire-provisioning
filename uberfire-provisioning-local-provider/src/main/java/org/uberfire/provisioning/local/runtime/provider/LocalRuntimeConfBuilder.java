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

package org.uberfire.provisioning.local.runtime.provider;

/**
 * @author salaboy
 */
public class LocalRuntimeConfBuilder {

    private static LocalRuntimeConfBuilder instance;
    private static LocalRuntimeConfiguration config;

    private LocalRuntimeConfBuilder() {
    }

    public static LocalRuntimeConfBuilder newConfig() {
        instance = new LocalRuntimeConfBuilder();
        config = new LocalRuntimeConfiguration();
        return instance;
    }

    public LocalRuntimeConfBuilder setProviderName( String providerName ) {
        config.setProviderName(providerName);
        return instance;
    }
    
    public LocalRuntimeConfBuilder setJar( String jar ) {
        config.setJar( jar );
        return instance;
    }

    public LocalRuntimeConfiguration get() {
        return config;
    }

}
