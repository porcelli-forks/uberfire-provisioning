package org.uberfire.provisioning.build.maven.executor;

import java.util.Optional;

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.config.MavenBuildConfig;
import org.uberfire.provisioning.build.maven.model.MavenBuild;
import org.uberfire.provisioning.config.BuildConfig;
import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.pipeline.BiFunctionConfigExecutor;

public class MavenBuildConfigExecutor implements BiFunctionConfigExecutor<Project, MavenBuildConfig, BuildConfig> {

    @Override
    public Optional<BuildConfig> apply( final Project project,
                                        final MavenBuildConfig mavenBuildConfig ) {
        return Optional.of( new MavenBuild( project, mavenBuildConfig.getGoals() ) );
    }

    @Override
    public Class<? extends Config> executeFor() {
        return MavenBuildConfig.class;
    }

    @Override
    public String outputId() {
        return "maven-config";
    }

    @Override
    public String inputId() {
        return "maven-config";
    }

}
