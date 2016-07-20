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
package org.uberfire.provisioning.openshift.runtime.provider;

import org.uberfire.provisioning.runtime.RuntimeConfiguration;

public class OpenshiftRuntimeConfBuilder {

    private static OpenshiftRuntimeConfBuilder instance;
    private static OpenshiftRuntimeConfiguration config;

    private OpenshiftRuntimeConfBuilder() {
    }

    public static OpenshiftRuntimeConfBuilder newConfig() {
        instance = new OpenshiftRuntimeConfBuilder();
        config = new OpenshiftRuntimeConfiguration();
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setNamespace( String namespace ) {
        config.setNamespace( namespace );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setServiceName( String serviceName ) {
        config.setServiceName( serviceName );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setReplicationController( String replicationController ) {
        config.setReplicationController( replicationController );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setLabel( String label ) {
        config.setLabel( label );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setImage( String image ) {
        config.setImage( image );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setInternalPort( String internalPort ) {
        config.setInternalPort( internalPort );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setContext( String context ) {
        config.setContext( context );
        return instance;
    }

    public OpenshiftRuntimeConfBuilder setProviderName( String providerName ) {
        config.setProviderName( providerName );
        return instance;
    }

    public RuntimeConfiguration get() {
        return config;
    }

}
