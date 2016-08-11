package org.uberfire.provisioning.build.maven.config;

import org.uberfire.provisioning.config.ProjectConfig;

public interface MavenProjectConfig extends ProjectConfig {

    default String getProjectDir() {
        return "${input.project-dir}";
    }
}
