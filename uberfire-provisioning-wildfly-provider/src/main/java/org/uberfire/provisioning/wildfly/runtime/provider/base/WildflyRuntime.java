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

import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.RuntimeEndpoint;
import org.uberfire.provisioning.runtime.RuntimeInfo;
import org.uberfire.provisioning.runtime.RuntimeState;
import org.uberfire.provisioning.runtime.base.BaseRuntime;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10RuntimeEndpoint;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10RuntimeInfo;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10RuntimeState;

public class WildflyRuntime extends BaseRuntime {

    public WildflyRuntime( String id,
            RuntimeConfiguration config,
            Provider provider ) {
        super( id, config, provider );
        if ( !( provider instanceof Wildfly10Provider ) ) {
            throw new IllegalArgumentException( "Wrong provider! set: " + provider.getClass() + " expected: WildflyProvider" );
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
    public RuntimeInfo getInfo() {
        return new Wildfly10RuntimeInfo();
    }

    @Override
    public RuntimeState getState() {
        return new Wildfly10RuntimeState();
    }

    @Override
    public RuntimeEndpoint getEndpoint() {
        String port = getConfig().getProperties().get( "port" );
        String host = getConfig().getProperties().get( "host" );
        String context = getConfig().getProperties().get( "context" );
        return new Wildfly10RuntimeEndpoint( host, new Integer( port ), context );
    }

}
