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

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.exceptions.BuildException;

import static java.util.Arrays.*;
import static java.util.Collections.*;

/**
 * @author salaboy
 *         The Build services implementation using Maven Invoker
 */
public class MavenBuild implements Build {

    @Override
    public int build( Project project ) throws BuildException {

        if ( !project.getType().equals( "Maven" ) ) {
            throw new BuildException( "This builder only support maven projects" );
        }
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( project.getRootPath() + "/" + project.getPath() + "/pom.xml" ) );
        request.setGoals( singletonList( "package" ) );

        Invoker invoker = new DefaultInvoker();

        try {
            InvocationResult results = invoker.execute( request );
            return results.getExitCode();
        } catch ( MavenInvocationException e ) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean binariesReady( Project project ) throws BuildException {
        return new File( project.getRootPath() + "/" + project.getPath() + "/target/" + project.getExpectedBinary() ).exists();
    }

    @Override
    public String binariesLocation( Project project ) throws BuildException {
        return project.getRootPath() + "/" + project.getPath() + "/target/" + project.getExpectedBinary();
    }

    /*
     * This method attemps to use the fabric8 docker maven plugin to build the docker image. 
     * for this to work you need to have the Docker Client running on your environment where this method is executed.
    */
    @Override
    public int createDockerImage( Project project ) throws BuildException {
        if ( !project.getType().equals( "Maven" ) ) {
            throw new BuildException( "This builder only support maven projects" );
        }
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( project.getRootPath() + "/" + project.getPath() + "/pom.xml" ) );
        request.setGoals( asList( "package", "docker:build" ) );

        Invoker invoker = new DefaultInvoker();

        try {
            InvocationResult results = invoker.execute( request );
            return results.getExitCode();
        } catch ( MavenInvocationException e ) {
            e.printStackTrace();
        }
        return -1;
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
