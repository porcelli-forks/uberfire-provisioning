/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlTransient;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.runtime.providers.base.BaseProvider;

/**
 * @author salaboy
 */
public class DockerProvider extends BaseProvider {

    @XmlTransient
    @JsonIgnore
    private DockerClient docker;

    public DockerProvider() {
    }
    
    public DockerProvider( ProviderConfiguration config ) {
        this( config, new DockerProviderType() );
    }

    public DockerProvider( ProviderConfiguration config,
                           ProviderType type ) {
        super( config.getName(), type );
        this.config = config;
        try {
            // If I wanted a custom connection to a custom configured docker deamon I should use here the information contained in CPI
            docker = DefaultDockerClient.fromEnv().build();
        } catch ( DockerCertificateException ex ) {
            Logger.getLogger( DockerProviderType.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    @Override
    public Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {

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

        List<PortBinding> randomPort = new ArrayList<PortBinding>();
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

        return new DockerRuntime( shortId, runtimeConfig, this );

    }

    public DockerClient getDocker() {
        return docker;
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
