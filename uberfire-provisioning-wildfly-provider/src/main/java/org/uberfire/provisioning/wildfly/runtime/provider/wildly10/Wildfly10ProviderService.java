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

import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderService;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntime;
import org.uberfire.provisioning.wildfly.runtime.provider.extras.Wildfly10RemoteClient;

import static java.util.UUID.*;

public class Wildfly10ProviderService implements ProviderService {

    private Wildfly10RemoteClient wildfly;

    private Wildfly10Provider provider;

    public Wildfly10ProviderService( Wildfly10Provider provider ) {
        this.provider = provider;
        /*
         I should check here for the required configuration parameters for the provider
         */
        wildfly = new Wildfly10RemoteClient();
    }

    @Override
    public org.uberfire.provisioning.runtime.Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {
        /*
         I should check here for the required configuration parameters for the runtime
         */

        String warPath = runtimeConfig.getProperties().get( "warPath" );
        String context = runtimeConfig.getProperties().get( "context" );
        String user = provider.getConfig().getProperties().get( "user" );
        String password = provider.getConfig().getProperties().get( "password" );
        String host = provider.getConfig().getProperties().get( "host" );
        String port = provider.getConfig().getProperties().get( "port" );
        String managementPort = provider.getConfig().getProperties().get( "managementPort" );

        int result = wildfly.deploy( user, password, host, new Integer( managementPort ), warPath );
        if ( result != 200 ) {
            throw new ProvisioningException( "Deployment to Wildfly Failed with error code: " + result );
        }
        String id = randomUUID().toString();
        String shortId = id.substring( 0, 12 );

        runtimeConfig.getProperties().put( "host", host );
        runtimeConfig.getProperties().put( "port", port );

        return new WildflyRuntime( shortId, runtimeConfig, provider );
    }

    @Override
    public void destroy( String runtimeId ) throws ProvisioningException {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    public Wildfly10RemoteClient getWildfly() {
        return wildfly;
    }
}
