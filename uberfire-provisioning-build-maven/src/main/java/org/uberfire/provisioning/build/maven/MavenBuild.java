/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.uberfire.provisioning.build.spi.Build;
import org.uberfire.provisioning.build.spi.Project;
import org.uberfire.provisioning.build.spi.exceptions.BuildException;

/**
 *
 * @author salaboy
 * The Build services implementation using Maven Invoker
 */
public class MavenBuild implements Build {

    @Override
    public int build(Project project) throws BuildException {

        if (!project.getType().equals("Maven")) {
            throw new BuildException("This builder only support maven projects");
        }
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(project.getRootPath() + "/" + project.getPath() + "/pom.xml"));
        request.setGoals(Collections.singletonList("package"));

        Invoker invoker = new DefaultInvoker();

        try {
            InvocationResult results = invoker.execute(request);
            return results.getExitCode();
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean binariesReady(Project project) throws BuildException {
        return new File(project.getRootPath() + "/" + project.getPath() + "/target/" + project.getExpectedBinary()).exists();
    }

    @Override
    public String binariesLocation(Project project) throws BuildException {
        return project.getRootPath() + "/" + project.getPath() + "/target/" + project.getExpectedBinary();
    }

    
    /*
     * This method attemps to use the fabric8 docker maven plugin to build the docker image. 
     * for this to work you need to have the Docker Client running on your environment where this method is executed.
    */
    @Override
    public int createDockerImage(Project project) throws BuildException {
        if (!project.getType().equals("Maven")) {
            throw new BuildException("This builder only support maven projects");
        }
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(project.getRootPath() + "/" + project.getPath() + "/pom.xml"));
        request.setGoals(Arrays.asList("package", "docker:build"));

        Invoker invoker = new DefaultInvoker();

        try {
            InvocationResult results = invoker.execute(request);
            return results.getExitCode();
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int cleanBinaries(Project project) throws BuildException {
        if (!project.getType().equals("Maven")) {
            throw new BuildException("This builder only support maven projects");
        }
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(project.getRootPath() + "/" + project.getPath() + "/pom.xml"));
        request.setGoals(Collections.singletonList("clean"));

        Invoker invoker = new DefaultInvoker();

        try {
            InvocationResult results = invoker.execute(request);
            return results.getExitCode();
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
