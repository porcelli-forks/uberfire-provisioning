/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.provisioning.build.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.uberfire.java.nio.file.Files;
import org.uberfire.java.nio.file.Path;
import org.uberfire.java.nio.file.Paths;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.exceptions.BuildException;

import static java.util.Collections.*;
import java.util.List;

/**
 *         The Build services implementation using Maven Invoker
 */
public class MavenBuild implements Build {

    private final Map<Project, RepositoryVisitor> projectVisitorMap = new HashMap<>();

    @Override
    public int build( final Project project ) throws BuildException {

        if ( !project.getType().equals( "Maven" ) ) {
            throw new BuildException( "This builder only support maven projects" );
        }

        return executeMaven( project, "package", "-DfailIfNoTests=false" );
    }

    @Override
    public boolean binariesReady( Project project ) throws BuildException {
        return Files.exists( binariesPath( project ) );
    }

    @Override
    public Path binariesPath( final Project project ) throws BuildException {
        if ( !projectVisitorMap.containsKey( project ) ) {
            throw new BuildException( "Project hasn't build." );
        }
        return Paths.get( new File( projectVisitorMap.get( project ).getTargetFolder(), project.getExpectedBinary() ).toURI() );
    }

    /*
     * This method attemps to use the fabric8 docker maven plugin to build the docker image. 
     * for this to work you need to have the Docker Client running on your environment where this method is executed.
    */
    @Override
    public int createDockerImage( Project project, boolean push, String username, String password ) throws BuildException {
        if ( !project.getType().equals( "Maven" ) ) {
            throw new BuildException( "This builder only support maven projects" );
        }
        List<String> goals = new ArrayList<>();
        goals.add("package");
        if(push){
            goals.add("-Ddocker.username="+username);
            goals.add("-Ddocker.password="+password);
        }
        goals.add("docker:build");
        goals.add("-DfailIfNoTests=false");
        if(push){
            goals.add("docker:push");
        }
        return executeMaven( project,  goals.toArray(new String[0]));
    }

    private int executeMaven( final Project project,
                              final String... goals ) {
        return new MavenCli().doMain( goals,
                                      getRepositoryVisitor( project ).getProjectFolder().getAbsolutePath(),
                                      System.out, System.out );
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

    @Override
    public int cleanBinaries( Project project ) throws BuildException {
        if ( !project.getType().equals( "Maven" ) ) {
            throw new BuildException( "This builder only support maven projects" );
        }
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( project.getRootPath() + "/" + project.getPath() + "/pom.xml" ) );
        request.setGoals( singletonList( "clean" ) );

        Invoker invoker = new DefaultInvoker();

        try {
            InvocationResult results = invoker.execute( request );
            return results.getExitCode();
        } catch ( MavenInvocationException e ) {
            e.printStackTrace();
        }
        return -1;
    }

}
