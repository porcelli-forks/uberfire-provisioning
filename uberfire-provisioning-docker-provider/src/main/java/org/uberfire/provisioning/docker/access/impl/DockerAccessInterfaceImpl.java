package org.uberfire.provisioning.docker.access.impl;

import java.util.HashMap;
import java.util.Map;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import org.uberfire.commons.lifecycle.Disposable;
import org.uberfire.provisioning.docker.access.DockerAccessInterface;
import org.uberfire.provisioning.runtime.providers.ProviderId;

public class DockerAccessInterfaceImpl
        implements DockerAccessInterface,
                   Disposable {

    final Map<String, DockerClient> clientMap = new HashMap<>();

    @Override
    public DockerClient getDockerClient( final ProviderId providerId ) {
        if ( !clientMap.containsKey( providerId.getId() ) ) {
            clientMap.put( providerId.getId(), buildClient( providerId ) );
        }
        return clientMap.get( providerId.getId() );
    }

    private DockerClient buildClient( final ProviderId providerId ) {
        if ( providerId.getId().equals( "local" ) ) {
            if ( System.getProperty( "os.name" ).toLowerCase().contains( "mac" ) ) {
                return DefaultDockerClient.builder().uri( DefaultDockerClient.DEFAULT_UNIX_ENDPOINT ).build();
            }
            try {
                return DefaultDockerClient.fromEnv().build();
            } catch ( DockerCertificateException e ) {
                throw new RuntimeException( e );
            }
        }

        return null;
    }

    @Override
    public void dispose() {
        clientMap.values().forEach( DockerClient::close );
    }
}
