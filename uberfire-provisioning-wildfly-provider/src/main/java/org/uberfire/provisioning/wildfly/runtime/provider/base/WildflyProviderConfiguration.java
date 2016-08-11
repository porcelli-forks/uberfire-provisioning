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

import org.uberfire.provisioning.runtime.providers.base.BaseProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderType;

/**
 * @author salaboy
 */
public class WildflyProviderConfiguration extends BaseProviderConfiguration {

    /*
     * This constructor shouldn't be used, The use of WildflyProviderConfiguration(String name)
     * is strictly recommended. This constructor is here just for the serializers to work correctly
    */
    public WildflyProviderConfiguration() {
        super( "", new Wildfly10ProviderType().getProvider().getName() );
    }

    public WildflyProviderConfiguration( String name ) {
        super( name, new Wildfly10ProviderType().getProvider().getName() );
    }

    public void setHost( String host ) {
        getProperties().put( "host", host );
    }

    public void setPort( String port ) {
        getProperties().put( "port", port );
    }

    public void setManagementPort( String port ) {
        getProperties().put( "managementPort", port );
    }

    public void setUser( String user ) {
        getProperties().put( "user", user );
    }

    public void setPassword( String password ) {
        getProperties().put( "password", password );
    }

    public String getHost() {
        return getProperties().get( "host" );
    }

    public String getManagementPort() {
        return getProperties().get( "port" );
    }

    public String getUser() {
        return getProperties().get( "user" );
    }

    public String getPassword() {
        return getProperties().get( "password" );
    }

}
