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
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.providers.ProviderService;
import org.uberfire.provisioning.runtime.Runtime;

public class DockerRuntimeService implements RuntimeService {

    protected ProviderService providerService;
    protected Runtime runtime;

    public DockerRuntimeService( ProviderService providerService, DockerRuntime runtime ) {
        this.providerService = providerService;
        this.runtime = runtime;

    }

    @Override
    public void start() {
        try {
            ( ( DockerProviderService ) providerService ).getDocker().startContainer( runtime.getId() );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerProvider.class.getName() ).log( SEVERE, null, ex );
        }

    }

    @Override
    public void stop() {
        try {
            ( ( DockerProviderService ) providerService ).getDocker().stopContainer( runtime.getId(), 0 );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerProvider.class.getName() ).log( SEVERE, null, ex );
        }
    }

    @Override
    public void restart() {

        try {
            ( ( DockerProviderService ) providerService ).getDocker().restartContainer( runtime.getId() );
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerProvider.class.getName() ).log( SEVERE, null, ex );
        }

    }

    @Override
    public void refresh() {
        try {
            ContainerInfo containerInfo = ( ( DockerProviderService ) providerService ).getDocker().inspectContainer( runtime.getId() );
            ContainerState state = containerInfo.state();
            String stateString = "NA";
            if ( state.running() ) {
                stateString = "Running";
            } else if ( state.paused() ) {
                stateString = "Paused";
            } else if ( state.restarting() ) {
                stateString = "Restarting";
            }
            runtime.setState( new DockerRuntimeState( stateString, state.startedAt().toString() ));
        } catch ( DockerException | InterruptedException ex ) {
            getLogger( DockerRuntime.class.getName() ).log( SEVERE, null, ex );
        }
        
    }
    
    

    @Override
    public ProviderService getProviderService() {
        return providerService;
    }

    @Override
    public void setProviderService( ProviderService provider ) {
        this.providerService = provider;
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
