package org.uberfire.provisioning.docker.config;

import org.uberfire.provisioning.config.ProviderConfig;

public interface DockerProviderConfig extends ProviderConfig {

    default String getName() {
        return "local";
    }

    default String getHostIp() {
        return "0.0.0.0";
    }

}
