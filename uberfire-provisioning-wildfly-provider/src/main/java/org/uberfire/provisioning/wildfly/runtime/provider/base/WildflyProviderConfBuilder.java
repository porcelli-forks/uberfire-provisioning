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

package org.uberfire.provisioning.wildfly.runtime.provider.base;

/**
 * @author salaboy
 */
public class WildflyProviderConfBuilder {

    private static WildflyProviderConfBuilder instance;
    private static WildflyProviderConfiguration config;

    private WildflyProviderConfBuilder() {

    }

    public static WildflyProviderConfBuilder newConfig( String providerName ) {
        instance = new WildflyProviderConfBuilder();
        config = new WildflyProviderConfiguration( providerName );
        return instance;
    }

    public WildflyProviderConfBuilder setName( String name ) {
        config.setName( name );
        return instance;
    }

    public WildflyProviderConfBuilder setHost( String host ) {
        config.setHost( host );
        return instance;
    }

    public WildflyProviderConfBuilder setPort( String port ) {
        config.setManagementPort( port );
        return instance;
    }

    public WildflyProviderConfBuilder setUser( String user ) {
        config.setUser( user );
        return instance;
    }

    public WildflyProviderConfBuilder setPassword( String password ) {
        config.setPassword( password );
        return instance;
    }

    public WildflyProviderConfiguration get() {
        return config;
    }

}
