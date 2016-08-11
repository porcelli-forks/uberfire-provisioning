package org.uberfire.provisioning.build.maven.executor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uberfire.provisioning.build.Binary;
import org.uberfire.provisioning.build.maven.config.MavenBuildConfig;
import org.uberfire.provisioning.build.maven.config.MavenBuildExecConfig;
import org.uberfire.provisioning.build.maven.config.MavenProjectConfig;
import org.uberfire.provisioning.config.BinaryConfig;
import org.uberfire.provisioning.config.BuildConfig;
import org.uberfire.provisioning.config.ProjectConfig;
import org.uberfire.provisioning.config.SourceConfig;
import org.uberfire.provisioning.pipeline.ContextAware;
import org.uberfire.provisioning.pipeline.Input;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineFactory;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.pipeline.execution.PipelineExecutor;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.registry.local.InMemoryBuildRegistry;
import org.uberfire.provisioning.registry.local.InMemorySourceRegistry;
import org.uberfire.provisioning.source.git.config.GitConfig;
import org.uberfire.provisioning.source.git.executor.GitConfigExecutor;

import static java.util.Arrays.*;
import static org.uberfire.provisioning.pipeline.StageUtil.*;

public class MavenProjectConfigExecutorTest {

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

        final Stage<Input, SourceConfig> sourceConfig = config( "Git Source", ( s ) -> new MyGitConfig() );
        final Stage<SourceConfig, ProjectConfig> projectConfig = config( "Maven Project", ( s ) -> new MavenProjectConfig() {
        } );
        final Stage<ProjectConfig, BuildConfig> buildConfig = config( "Maven Build Config", ( s ) -> new MavenBuildConfig() {
        } );
        final Stage<BuildConfig, BinaryConfig> buildExec = config( "Maven Build", ( s ) -> new MavenBuildExecConfig() {
        } );
        final Pipeline pipe = PipelineFactory
                .startFrom( sourceConfig )
                .andThen( projectConfig )
                .andThen( buildConfig )
                .andThen( buildExec ).buildAs( "my pipe" );

        final PipelineExecutor executor = new PipelineExecutor( asList( new GitConfigExecutor( sourceRegistry ), new MavenProjectConfigExecutor( sourceRegistry ), new MavenBuildConfigExecutor(), new MavenBuildExecConfigExecutor( buildRegistry ) ) );
        executor.execute( new Input() {{
            put( "repo-name", "livespark-playground" );
            put( "out-dir", tempPath.getAbsolutePath() );
            put( "origin", "https://github.com/pefernan/livespark-playground" );
            put( "project-dir", "users-new" );
        }}, pipe, ( Binary b ) -> System.out.println( b.getName() ) );
    }

    static class MyGitConfig implements GitConfig,
                                        ContextAware {

        private Map<String, ?> context;

        @Override
        public void setContext( final Map<String, ?> context ) {
            this.context = context;
        }

        @Override
        public String getRepoName() {
            return ( (Map) context.get( "input" ) ).get( "repo-name" ).toString();
        }

        @Override
        public String getBranch() {
            return "master";
        }

    }

}
