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

import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.providers.ProviderService;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderService;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10RuntimeEndpoint;

public class WildflyRuntimeService implements RuntimeService {

    private ProviderService providerService;
    protected org.uberfire.provisioning.runtime.Runtime runtime;

    public WildflyRuntimeService( ProviderService providerService, Runtime runtime ) {
        this.providerService = providerService;
        this.runtime = runtime;
        if ( !( providerService instanceof Wildfly10ProviderService ) ) {
            throw new IllegalArgumentException( "Wrong provider service! set: " + providerService.getClass() + " expected: WildflyProviderService" );
        }
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void restart() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh() {
        String port = runtime.getConfig().getProperties().get( "port" );
        String host = runtime.getConfig().getProperties().get( "host" );
        String context = runtime.getConfig().getProperties().get( "context" );
        runtime.setEndpoint( new Wildfly10RuntimeEndpoint( host, new Integer( port ), context ) );
    }

    @Override
    public ProviderService getProviderService() {
        return providerService;
    }

    @Override
    public void setProviderService( ProviderService providerService ) {
        this.providerService = providerService;
    }

    @Override
    public Runtime getRuntime() {
        return runtime;
    }

    @Override
    public void setRuntime( Runtime runtime ) {
        this.runtime = runtime;
    }
}
