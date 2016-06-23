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

import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ContainerState;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.RuntimeInfo;
import org.uberfire.provisioning.runtime.RuntimeState;
import org.uberfire.provisioning.runtime.base.BaseRuntime;
import org.uberfire.provisioning.runtime.base.BaseRuntimeState;
import org.uberfire.provisioning.runtime.providers.Provider;

import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;
import org.uberfire.provisioning.runtime.RuntimeEndpoint;
import org.uberfire.provisioning.runtime.base.BaseRuntimeEndpoint;

/**
 * @author salaboy
 */
public class DockerRuntime extends BaseRuntime {

    public DockerRuntime( String id,
            RuntimeConfiguration config,
            Provider provider ) {
        super( id, config, provider );
        if ( !( provider instanceof DockerProvider ) ) {
            throw new IllegalArgumentException( "Wrong provider! set: " + provider.getClass() + " expected: DockerProvider" );
        }

    }

    @Override
    public void start() {
        try {
            ( ( DockerProvider ) provider ).getDocker().startContainer( getId() );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerProvider.class.getName() ).log( SEVERE, null, ex );
        }

    }

    @Override
    public void stop() {

        try {
            ( ( DockerProvider ) provider ).getDocker().stopContainer( getId(), 0 );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerProvider.class.getName() ).log( SEVERE, null, ex );
        }

    }

    @Override
    public void restart() {

        try {
            ( ( DockerProvider ) provider ).getDocker().restartContainer( getId() );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerProvider.class.getName() ).log( SEVERE, null, ex );
        }

    }

    @Override
    public RuntimeInfo getInfo() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuntimeState getState() {
        try {
            ContainerInfo containerInfo = ( ( DockerProvider ) provider ).getDocker().inspectContainer( getId() );
            ContainerState state = containerInfo.state();
            String stateString = "NA";
            if ( state.running() ) {
                stateString = "Running";
            } else if ( state.paused() ) {
                stateString = "Paused";
            } else if ( state.restarting() ) {
                stateString = "Restarting";
            }
            return new BaseRuntimeState( stateString, state.startedAt().toString() );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerRuntime.class.getName() ).log( SEVERE, null, ex );
        }
        return null;
    }

    @Override
    public RuntimeEndpoint getEndpoint() {
        return new BaseRuntimeEndpoint();
    }

}
