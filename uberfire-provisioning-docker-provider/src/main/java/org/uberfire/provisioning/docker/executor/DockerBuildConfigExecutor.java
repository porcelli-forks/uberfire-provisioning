package org.uberfire.provisioning.docker.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.uberfire.provisioning.build.maven.model.MavenBuild;
import org.uberfire.provisioning.config.BuildConfig;
import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.docker.config.DockerBuildConfig;
import org.uberfire.provisioning.docker.model.DockerBuild;
import org.uberfire.provisioning.pipeline.BiFunctionConfigExecutor;

public class DockerBuildConfigExecutor implements BiFunctionConfigExecutor<MavenBuild, DockerBuildConfig, BuildConfig> {

    @Override
    public Optional<BuildConfig> apply( final MavenBuild buildConfig,
                                        final DockerBuildConfig dockerBuildConfig ) {
        final List<String> goals = new ArrayList<>( buildConfig.getGoals() );
        if ( dockerBuildConfig.push() ) {
            goals.add( "-Ddocker.username=" + dockerBuildConfig.getUsername() );
            goals.add( "-Ddocker.password=" + dockerBuildConfig.getPassword() );
        }
        goals.add( "docker:build" );
        if ( dockerBuildConfig.push() ) {
            goals.add( "docker:push" );
        }
        return Optional.of( new DockerBuild( buildConfig.getProject(), goals ) );
    }

    @Override
    public Class<? extends Config> executeFor() {
        return DockerBuildConfig.class;
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
