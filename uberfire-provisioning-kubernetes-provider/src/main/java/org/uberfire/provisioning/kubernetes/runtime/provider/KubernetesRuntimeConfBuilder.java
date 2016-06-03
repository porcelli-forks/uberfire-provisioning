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

/**
 * @author salaboy
 */
public class KubernetesRuntimeConfBuilder {

    private static KubernetesRuntimeConfBuilder instance;
    private static KubernetesRuntimeConfiguration config;

    private KubernetesRuntimeConfBuilder() {
    }

    public static KubernetesRuntimeConfBuilder newConfig() {
        instance = new KubernetesRuntimeConfBuilder();
        config = new KubernetesRuntimeConfiguration();
        return instance;
    }

    public KubernetesRuntimeConfBuilder setNamespace( String namespace ) {
        config.setNamespace( namespace );
        return instance;
    }

    public KubernetesRuntimeConfBuilder setServiceName( String serviceName ) {
        config.setServiceName( serviceName );
        return instance;
    }

    public KubernetesRuntimeConfBuilder setReplicationController( String replicationController ) {
        config.setReplicationController( replicationController );
        return instance;
    }

    public KubernetesRuntimeConfBuilder setLabel( String label ) {
        config.setLabel( label );
        return instance;
    }

    public KubernetesRuntimeConfBuilder setImage( String image ) {
        config.setImage( image );
        return instance;
    }

    public KubernetesRuntimeConfiguration get() {
        return config;
    }

}
