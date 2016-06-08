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

package org.uberfire.provisioning.wildfly.runtime.provider.wildly10;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.runtime.providers.base.BaseProvider;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntime;
import org.uberfire.provisioning.wildfly.util.Wildfly10RemoteClient;

import static java.util.UUID.*;

/**
 * @author salaboy
 */
public class Wildfly10Provider extends BaseProvider {

    @XmlTransient
    @JsonIgnore
    private Wildfly10RemoteClient wildfly;

    public Wildfly10Provider() {
    }

    public Wildfly10Provider( ProviderConfiguration config ) {
        this( config, new Wildfly10ProviderType() );

    }

    public Wildfly10Provider( ProviderConfiguration config,
                              ProviderType type ) {
        super( config.getName(), type );
        /*
         I should check here for the required configuration parameters for the provider
         */
        wildfly = new Wildfly10RemoteClient();
        this.config = config;
    }

    @Override
    public Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {
        /*
         I should check here for the required configuration parameters for the runtime
         */

        String warPath = runtimeConfig.getProperties().get( "warPath" );
        String user = config.getProperties().get( "user" );
        String password = config.getProperties().get( "password" );
        String host = config.getProperties().get( "host" );
        String port = config.getProperties().get( "port" );

        int result = wildfly.deploy( user, password, host, new Integer( port ), warPath );
        if ( result != 200 ) {
            throw new ProvisioningException( "Deployment to Wildfly Failed with error code: " + result );
        }
        String id = randomUUID().toString();
        String shortId = id.substring( 0, 12 );

        return new WildflyRuntime( shortId, runtimeConfig, this );
    }

    @Override
    public void destroy( String runtimeId ) throws ProvisioningException {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    public Wildfly10RemoteClient getWildfly() {
        return wildfly;
    }

}
