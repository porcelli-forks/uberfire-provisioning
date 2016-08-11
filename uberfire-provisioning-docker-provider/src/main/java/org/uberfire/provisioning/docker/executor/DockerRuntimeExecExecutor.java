package org.uberfire.provisioning.docker.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.config.RuntimeConfig;
import org.uberfire.provisioning.docker.access.DockerAccessInterface;
import org.uberfire.provisioning.docker.config.DockerRuntimeConfiguration;
import org.uberfire.provisioning.docker.config.DockerRuntimeExecConfig;
import org.uberfire.provisioning.docker.model.DockerProvider;
import org.uberfire.provisioning.docker.model.DockerRuntime;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.pipeline.FunctionConfigExecutor;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.RuntimeBuilder;
import org.uberfire.provisioning.runtime.RuntimeDestroyer;
import org.uberfire.provisioning.runtime.RuntimeId;

public class DockerRuntimeExecExecutor<T extends DockerRuntimeConfiguration> implements RuntimeBuilder<T, DockerRuntime>,
                                                                                        RuntimeDestroyer,
                                                                                        FunctionConfigExecutor<T, DockerRuntime> {

    private final RuntimeRegistry runtimeRegistry;
    private final DockerAccessInterface docker;

    @Inject
    public DockerRuntimeExecExecutor( final RuntimeRegistry runtimeRegistry,
                                      final DockerAccessInterface docker ) {
        this.runtimeRegistry = runtimeRegistry;
        this.docker = docker;
    }

    @Override
    public Optional<DockerRuntime> apply( final DockerRuntimeConfiguration config ) {
        final Optional<DockerRuntime> runtime = create( config );
        if ( runtime.isPresent() ) {
            runtimeRegistry.registerRuntime( runtime.get() );
        }
        return runtime;
    }

    private Optional<DockerRuntime> create( final DockerRuntimeConfiguration runtimeConfig ) throws ProvisioningException {
        if ( runtimeConfig.isPull() ) {
            try {
                docker.getDockerClient( runtimeConfig.getProviderId() ).pull( runtimeConfig.getImage() );
            } catch ( DockerException | InterruptedException ex ) {
                Logger.getLogger( DockerProvider.class.getName() ).log( Level.SEVERE, null, ex );
                throw new ProvisioningException( "Error Pulling Docker Image: " + runtimeConfig.getImage() + "with error: " + ex.getMessage() );
            }
        }

        final String[] ports = { runtimeConfig.getPort() };
        final Map<String, List<PortBinding>> portBindings = new HashMap<>();

        final Optional<DockerProvider> _dockerProvider = runtimeRegistry.getProvider( runtimeConfig.getProviderId(), DockerProvider.class );

        if ( !_dockerProvider.isPresent() ) {
            return Optional.empty();
        }
        final DockerProvider dockerProvider = _dockerProvider.get();
        final List<PortBinding> randomPort = new ArrayList<>();
        final PortBinding randomPortBinding = PortBinding.randomPort( dockerProvider.getHostId() );

        randomPort.add( randomPortBinding );
        portBindings.put( runtimeConfig.getPort(), randomPort );

        final HostConfig hostConfig = HostConfig.builder().portBindings( portBindings ).build();

        final ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig( hostConfig )
                .image( runtimeConfig.getImage() )
                .exposedPorts( ports )
                .build();

        final ContainerCreation creation;
        try {
            creation = docker.getDockerClient( runtimeConfig.getProviderId() ).createContainer( containerConfig );
        } catch ( DockerException | InterruptedException ex ) {
            Logger.getLogger( DockerProvider.class.getName() ).log( Level.SEVERE, null, ex );
            throw new ProvisioningException( "Error Creating Docker Container with image: " + runtimeConfig.getImage() + "with error: " + ex.getMessage() );
        }

        final String id = creation.id();
        String shortId = id.substring( 0, 12 );

        return Optional.of( new DockerRuntime( shortId, runtimeConfig, dockerProvider ) );
    }

    @Override
    public Class<? extends Config> executeFor() {
        return DockerRuntimeExecConfig.class;
    }

    @Override
    public String outputId() {
        return "docker-runtime";
    }

    @Override
    public boolean supports( final RuntimeConfig config ) {
        return config instanceof DockerRuntimeConfiguration;
    }

    @Override
    public boolean supports( final RuntimeId runtimeId ) {
        return runtimeId instanceof DockerRuntime ||
                runtimeRegistry.getRuntimeById( runtimeId.getId() ) instanceof DockerRuntime;
    }

    @Override
    public void destroy( final RuntimeId runtimeId ) {
        try {
            docker.getDockerClient( runtimeId.getProviderId() ).killContainer( runtimeId.getId() );
            docker.getDockerClient( runtimeId.getProviderId() ).removeContainer( runtimeId.getId() );
            runtimeRegistry.unregisterRuntime( runtimeId );
        } catch ( DockerException | InterruptedException ex ) {
            Logger.getLogger( DockerProvider.class.getName() ).log( Level.SEVERE, null, ex );
            throw new ProvisioningException( "Error destroying Docker Runtime: " + ex.getMessage() );
        }
    }
}
