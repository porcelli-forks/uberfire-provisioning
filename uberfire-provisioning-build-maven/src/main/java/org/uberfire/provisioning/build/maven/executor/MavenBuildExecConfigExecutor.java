package org.uberfire.provisioning.build.maven.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

import org.apache.maven.cli.MavenCli;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.config.MavenBuildExecConfig;
import org.uberfire.provisioning.build.maven.model.MavenBinary;
import org.uberfire.provisioning.build.maven.model.MavenBuild;
import org.uberfire.provisioning.build.maven.util.RepositoryVisitor;
import org.uberfire.provisioning.config.BinaryConfig;
import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.pipeline.BiFunctionConfigExecutor;
import org.uberfire.provisioning.registry.BuildRegistry;

public class MavenBuildExecConfigExecutor implements BiFunctionConfigExecutor<MavenBuild, MavenBuildExecConfig, BinaryConfig> {

    private final BuildRegistry buildRegistry;

    @Inject
    public MavenBuildExecConfigExecutor( final BuildRegistry buildRegistry ) {
        this.buildRegistry = buildRegistry;
    }

    @Override
    public Optional<BinaryConfig> apply( final MavenBuild mavenBuild,
                                         final MavenBuildExecConfig mavenBuildExecConfig ) {
        build( mavenBuild.getProject(), mavenBuild.getGoals() );
        final MavenBinary binary = new MavenBinary( mavenBuild.getProject() );
        buildRegistry.registerBinary( binary );
        return Optional.of( binary );
    }

    @Override
    public Class<? extends Config> executeFor() {
        return MavenBuildExecConfig.class;
    }

    @Override
    public String outputId() {
        return "binary";
    }

    @Override
    public String inputId() {
        return "maven-exec-config";
    }

    private final Map<Project, RepositoryVisitor> projectVisitorMap = new HashMap<>();

    public int build( final Project project,
                      final List<String> goals ) throws BuildException {
        return executeMaven( project, goals.toArray( new String[]{} ) );
    }

    //    public Path binariesPath( final Project project ) throws BuildException {
//        if ( !projectVisitorMap.containsKey( project ) ) {
//            throw new BuildException( "Project hasn't build." );
//        }
//        return Paths.get( new File( projectVisitorMap.get( project ).getTargetFolder(), project.getExpectedBinary() ).toURI() );
//    }
//
    private int executeMaven( final Project project,
                              final String... goals ) {
        return new MavenCli().doMain( goals,
                                      getRepositoryVisitor( project ).getProjectFolder().getAbsolutePath(),
                                      System.err, System.err );
    }

    private RepositoryVisitor getRepositoryVisitor( final Project project ) {
        final RepositoryVisitor projectVisitor;
        if ( projectVisitorMap.containsKey( project ) ) {
            projectVisitor = projectVisitorMap.get( project );
        } else {
            projectVisitor = new RepositoryVisitor( project );
            projectVisitorMap.put( project, projectVisitor );
        }
        return projectVisitor;
    }

//    //    @Override
//    public int cleanBinaries( Project project ) throws BuildException {
//        if ( !project.getType().equals( "Maven" ) ) {
//            throw new BuildException( "This builder only support maven projects" );
//        }
//        InvocationRequest request = new DefaultInvocationRequest();
//        request.setPomFile( new File( project.getRootPath() + "/" + project.getPath() + "/pom.xml" ) );
//        request.setGoals( singletonList( "clean" ) );
//
//        Invoker invoker = new DefaultInvoker();
//
//        try {
//            InvocationResult results = invoker.execute( request );
//            return results.getExitCode();
//        } catch ( MavenInvocationException e ) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
}
