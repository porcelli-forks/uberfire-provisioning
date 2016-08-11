package org.uberfire.provisioning.docker.executor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uberfire.provisioning.build.maven.config.MavenBuildConfig;
import org.uberfire.provisioning.build.maven.config.MavenBuildExecConfig;
import org.uberfire.provisioning.build.maven.config.MavenProjectConfig;
import org.uberfire.provisioning.build.maven.executor.MavenBuildConfigExecutor;
import org.uberfire.provisioning.build.maven.executor.MavenBuildExecConfigExecutor;
import org.uberfire.provisioning.build.maven.executor.MavenProjectConfigExecutor;
import org.uberfire.provisioning.config.BinaryConfig;
import org.uberfire.provisioning.config.BuildConfig;
import org.uberfire.provisioning.config.ProjectConfig;
import org.uberfire.provisioning.config.ProviderConfig;
import org.uberfire.provisioning.config.ProvisioningConfig;
import org.uberfire.provisioning.config.RuntimeConfig;
import org.uberfire.provisioning.config.SourceConfig;
import org.uberfire.provisioning.docker.access.DockerAccessInterface;
import org.uberfire.provisioning.docker.access.impl.DockerAccessInterfaceImpl;
import org.uberfire.provisioning.docker.config.ContextAwareDockerProvisioningConfig;
import org.uberfire.provisioning.docker.config.ContextAwareDockerRuntimeExecConfig;
import org.uberfire.provisioning.docker.config.DockerBuildConfig;
import org.uberfire.provisioning.docker.config.DockerProviderConfig;
import org.uberfire.provisioning.pipeline.Input;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineFactory;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.pipeline.execution.PipelineExecutor;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.registry.local.InMemoryBuildRegistry;
import org.uberfire.provisioning.registry.local.InMemoryRuntimeRegistry;
import org.uberfire.provisioning.registry.local.InMemorySourceRegistry;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.source.git.config.GitConfig;
import org.uberfire.provisioning.source.git.executor.GitConfigExecutor;

import static java.util.Arrays.*;
import static org.uberfire.provisioning.pipeline.StageUtil.*;

/**
 * TODO: update me
 */
public class DockerExecutorTest {

    private File tempPath;

    @Before
    public void setUp() {
        try {
            tempPath = Files.createTempDirectory( "xxx" ).toFile();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        FileUtils.deleteQuietly( tempPath );
    }

    @Test
    public void testAPI() {
        final SourceRegistry sourceRegistry = new InMemorySourceRegistry();
        final BuildRegistry buildRegistry = new InMemoryBuildRegistry();
        final InMemoryRuntimeRegistry runtimeRegistry = new InMemoryRuntimeRegistry();
        final DockerAccessInterface dockerAccessInterface = new DockerAccessInterfaceImpl();

        final Stage<Input, SourceConfig> sourceConfig = config( "Git Source", ( s ) -> new GitConfig() {
        } );
        final Stage<SourceConfig, ProjectConfig> projectConfig = config( "Maven Project", ( s ) -> new MavenProjectConfig() {
        } );
        final Stage<ProjectConfig, BuildConfig> buildConfig = config( "Maven Build Config", ( s ) -> new MavenBuildConfig() {
        } );
        final Stage<BuildConfig, BuildConfig> dockerBuildConfig = config( "Docker Build Config", ( s ) -> new DockerBuildConfig() {
        } );
        final Stage<BuildConfig, BinaryConfig> buildExec = config( "Maven Build", ( s ) -> new MavenBuildExecConfig() {
        } );
        final Stage<BinaryConfig, ProviderConfig> providerConfig = config( "Docker Provider Config", ( s ) -> new DockerProviderConfig() {
        } );

        final Stage<ProviderConfig, ProvisioningConfig> runtimeConfig = config( "Docker Runtime Config", ( s ) -> new ContextAwareDockerProvisioningConfig() {
        } );

        final Stage<ProvisioningConfig, RuntimeConfig> runtimeExec = config( "Docker Runtime Exec", ( s ) -> new ContextAwareDockerRuntimeExecConfig() );

        final Pipeline pipe = PipelineFactory
                .startFrom( sourceConfig )
                .andThen( projectConfig )
                .andThen( buildConfig )
                .andThen( dockerBuildConfig )
                .andThen( buildExec )
                .andThen( providerConfig )
                .andThen( runtimeConfig )
                .andThen( runtimeExec ).buildAs( "my pipe" );

        final PipelineExecutor executor = new PipelineExecutor( asList( new GitConfigExecutor( sourceRegistry ),
                                                                        new MavenProjectConfigExecutor( sourceRegistry ),
                                                                        new MavenBuildConfigExecutor(),
                                                                        new MavenBuildExecConfigExecutor( buildRegistry ),
                                                                        new DockerBuildConfigExecutor(),
                                                                        new DockerProviderConfigExecutor( runtimeRegistry ),
                                                                        new DockerProvisioningConfigExecutor(),
                                                                        new DockerRuntimeExecExecutor( runtimeRegistry, dockerAccessInterface ) ) );
        executor.execute( new Input() {{
            put( "repo-name", "livespark-playground" );
            put( "branch", "master" );
            put( "out-dir", tempPath.getAbsolutePath() );
            put( "origin", "https://github.com/pefernan/livespark-playground" );
            put( "project-dir", "users-new" );
        }}, pipe, ( Runtime b ) -> System.out.println( b ) );

        dockerAccessInterface.dispose();
    }

    @Test
    public void testFlexAPI() {
        final InMemoryRuntimeRegistry runtimeRegistry = new InMemoryRuntimeRegistry();
        final DockerAccessInterface dockerAccessInterface = new DockerAccessInterfaceImpl();

        final Stage<Input, ProviderConfig> providerConfig = config( "Docker Provider Config", ( s ) -> new DockerProviderConfig() {
        } );

        final Stage<ProviderConfig, ProvisioningConfig> runtimeConfig = config( "Docker Runtime Config", ( s ) -> new ContextAwareDockerProvisioningConfig() {
        } );

        final Stage<ProvisioningConfig, RuntimeConfig> runtimeExec = config( "Docker Runtime Exec", ( s ) -> new ContextAwareDockerRuntimeExecConfig() );

        final Pipeline pipe = PipelineFactory
                .startFrom( providerConfig )
                .andThen( runtimeConfig )
                .andThen( runtimeExec ).buildAs( "my pipe" );

        final PipelineExecutor executor = new PipelineExecutor( asList( new DockerProviderConfigExecutor( runtimeRegistry ),
                                                                        new DockerProvisioningConfigExecutor(),
                                                                        new DockerRuntimeExecExecutor( runtimeRegistry, dockerAccessInterface ) ) );
        executor.execute( new Input() {{
            put( "image-name", "salaboy/users-new" );
            put( "port-number", "8080" );
        }}, pipe, ( Runtime b ) -> System.out.println( b ) );

        dockerAccessInterface.dispose();
    }
}
