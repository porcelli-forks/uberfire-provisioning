package org.uberfire.provisioning.docker.executor;

import java.util.Optional;

import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.docker.config.DockerProvisioningConfig;
import org.uberfire.provisioning.docker.config.DockerRuntimeConfiguration;
import org.uberfire.provisioning.pipeline.FunctionConfigExecutor;
import org.uberfire.provisioning.runtime.providers.ProviderId;

public class DockerProvisioningConfigExecutor implements
                                              FunctionConfigExecutor<DockerProvisioningConfig, DockerRuntimeConfiguration> {

    @Override
    public Optional<DockerRuntimeConfiguration> apply( final DockerProvisioningConfig dockerRuntimeConfig ) {
        return Optional.of( new DockerRuntimeConfiguration() {
            @Override
            public ProviderId getProviderId() {
                return dockerRuntimeConfig.getProviderId();
            }

            @Override
            public String getImage() {
                return dockerRuntimeConfig.getImageName();
            }

            @Override
            public String getPort() {
                return dockerRuntimeConfig.getPortNumber();
            }

            @Override
            public boolean isPull() {
                return dockerRuntimeConfig.pull();
            }
        } );
    }

    @Override
    public Class<? extends Config> executeFor() {
        return DockerProvisioningConfig.class;
    }

    @Override
    public String outputId() {
        return "docker-runtime-config";
    }

}
