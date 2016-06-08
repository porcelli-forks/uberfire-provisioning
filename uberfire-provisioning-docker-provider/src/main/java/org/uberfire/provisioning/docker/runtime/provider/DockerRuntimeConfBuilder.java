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

package org.uberfire.provisioning.docker.runtime.provider;

import org.uberfire.provisioning.runtime.RuntimeConfiguration;

/**
 * @author salaboy
 */
public class DockerRuntimeConfBuilder {

    private static DockerRuntimeConfBuilder instance;
    private static DockerRuntimeConfiguration config;

    private DockerRuntimeConfBuilder() {
    }

    public static DockerRuntimeConfBuilder newConfig() {
        instance = new DockerRuntimeConfBuilder();
        config = new DockerRuntimeConfiguration();
        return instance;
    }

    public DockerRuntimeConfBuilder setImage( String image ) {
        config.setImage( image );
        return instance;
    }

    public DockerRuntimeConfBuilder setPull( boolean pull ) {
        config.setPull( pull );
        return instance;
    }

    public DockerRuntimeConfBuilder setProviderName( String providerName ) {
        config.setProviderName(providerName);
        return instance;
    }
    
    public RuntimeConfiguration get() {
        return config;
    }

}
