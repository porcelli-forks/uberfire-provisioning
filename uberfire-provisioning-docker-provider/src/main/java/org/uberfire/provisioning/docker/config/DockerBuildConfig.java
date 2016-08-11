package org.uberfire.provisioning.docker.config;

import org.uberfire.provisioning.config.BuildConfig;

public interface DockerBuildConfig extends BuildConfig {

    default String getUsername() {
        return "${input.docker-user}";
    }

    default String getPassword() {
        return "${input.docker-password}";
    }

    default boolean push() {
        return Boolean.parseBoolean( "${input.docker-push}" );
    }

}
