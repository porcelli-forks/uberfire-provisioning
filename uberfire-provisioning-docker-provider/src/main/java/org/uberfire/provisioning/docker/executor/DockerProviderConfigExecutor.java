package org.uberfire.provisioning.docker.executor;

import java.util.Optional;

import javax.inject.Inject;

import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.config.ProviderConfig;
import org.uberfire.provisioning.docker.config.DockerProviderConfig;
import org.uberfire.provisioning.docker.model.DockerProvider;
import org.uberfire.provisioning.pipeline.FunctionConfigExecutor;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.providers.ProviderBuilder;
import org.uberfire.provisioning.runtime.providers.ProviderDestroyer;
import org.uberfire.provisioning.runtime.providers.ProviderId;

public class DockerProviderConfigExecutor implements ProviderBuilder<DockerProviderConfig, DockerProvider>,
                                                     ProviderDestroyer,
                                                     FunctionConfigExecutor<DockerProviderConfig, DockerProvider> {

    private final RuntimeRegistry runtimeRegistry;

    @Inject
    public DockerProviderConfigExecutor( final RuntimeRegistry runtimeRegistry ) {
        this.runtimeRegistry = runtimeRegistry;
    }

    @Override
    public Optional<DockerProvider> apply( final DockerProviderConfig dockerProviderConfig ) {
        final DockerProvider provider = new DockerProvider( dockerProviderConfig.getName(), dockerProviderConfig.getHostIp() );
        runtimeRegistry.registerProvider( provider );
        return Optional.of( provider );
    }

    @Override
    public Class<? extends Config> executeFor() {
        return DockerProviderConfig.class;
    }

    @Override
    public String outputId() {
        return "docker-provider";
    }

    @Override
    public boolean supports( final ProviderConfig config ) {
        return config instanceof DockerProviderConfig;
    }

    @Override
    public boolean supports( final ProviderId providerId ) {
        return providerId instanceof DockerProvider;
    }

    @Override
    public void destroy( final ProviderId providerId ) {
    }

}
