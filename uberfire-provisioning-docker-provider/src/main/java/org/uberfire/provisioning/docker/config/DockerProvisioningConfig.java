package org.uberfire.provisioning.docker.config;

import org.uberfire.provisioning.config.ProvisioningConfig;

public interface DockerProvisioningConfig extends ProvisioningConfig {

    default String getImageName() {
        return "${input.image-name}";
    }

    default String getPortNumber() {
        return "${input.port-number}";
    }

    default boolean pull() {
        return Boolean.valueOf( dockerPullValue() );
    }

    default String dockerPullValue() {
        return "${input.docker-pull}";
    }

}
