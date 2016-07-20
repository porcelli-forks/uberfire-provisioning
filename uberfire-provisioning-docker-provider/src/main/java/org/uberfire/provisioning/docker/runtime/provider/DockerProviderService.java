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

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderService;

public class DockerProviderService implements ProviderService {

    private DockerClient docker;
    private DockerProvider provider;

    public DockerProviderService( DockerProvider provider ) {

        this.provider = provider;
        try {
            // If I wanted a custom connection to a custom configured docker deamon I should use here the information contained in CPI
            docker = DefaultDockerClient.fromEnv().build();
        } catch ( DockerCertificateException ex ) {
            Logger.getLogger( DockerProviderType.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    @Override
    public org.uberfire.provisioning.runtime.Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {

        if ( runtimeConfig.getProperties().get( "pull" ) != null && runtimeConfig.getProperties().get( "pull" ).equals( "true" ) ) {
            try {
                docker.pull( runtimeConfig.getProperties().get( "image" ) );
            } catch ( DockerException | InterruptedException ex ) {
                Logger.getLogger( DockerProvider.class.getName() ).log( Level.SEVERE, null, ex );
                throw new ProvisioningException( "Error Pulling Docker Image: " + runtimeConfig.getProperties().get( "image" ) + "with error: " + ex.getMessage() );
            }
        }

        final String[] ports = { "8080" };
        final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();

        List<PortBinding> randomPort = new ArrayList<>();
        PortBinding randomPortBinding = PortBinding.randomPort( "0.0.0.0" );
        randomPort.add( randomPortBinding );
        portBindings.put( "8080", randomPort );

        final HostConfig hostConfig = HostConfig.builder().portBindings( portBindings ).build();

        final ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig( hostConfig )
                .image( runtimeConfig.getProperties().get( "image" ) ).exposedPorts( ports )
                .build();

        ContainerCreation creation = null;
        try {
            creation = docker.createContainer( containerConfig );
        } catch ( DockerException | InterruptedException ex ) {
            Logger.getLogger( DockerProvider.class.getName() ).log( Level.SEVERE, null, ex );
            throw new ProvisioningException( "Error Creating Docker Container with image: " + runtimeConfig.getProperties().get( "image" ) + "with error: " + ex.getMessage() );
        }

        final String id = creation.id();
        String shortId = id.substring( 0, 12 );

        return new DockerRuntime( shortId, runtimeConfig, provider );

    }

    public DockerClient getDocker() {
        return docker;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider( DockerProvider provider ) {
        this.provider = provider;
    }

    @Override
    public void destroy( String runtimeId ) throws ProvisioningException {
        try {
            docker.killContainer( runtimeId );
            docker.removeContainer( runtimeId );
        } catch ( DockerException | InterruptedException ex ) {
            Logger.getLogger( DockerProvider.class.getName() ).log( Level.SEVERE, null, ex );
            throw new ProvisioningException( "Error destroying Docker Runtime: " + ex.getMessage() );
        }

    }
}
