/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.spi;

import org.uberfire.provisioning.build.spi.exceptions.BuildException;

/**
 *
 * @author salaboy
 * The Build interface define a set of operations to generate
 * Binaries based on Projects
 * 
 */
public interface Build {

    /*
    *   Build the specified Project
    *   @param project a Project to build
    *   @throws BuildException if the build failed unexpectedly 
    *   @return the build output 0 if there wasn't an error
    *   @see Project
    */
    public int build(Project project) throws BuildException;

    /*
    * Check for the generated binaries for a given project
    * @param project a Project to check if the binaries are ready
    * @return true if the binaries are already generated
    * @see Project
    */
    public boolean binariesReady(Project project) throws BuildException;
    
    /* 
    * Returns the location of the binaries if exist
    * @param project the project for the binaries that we are looking for 
    * @return String with the location of the binaries
    * @see Project
    */
    public String binariesLocation(Project project) throws BuildException;
    
    /* 
    * Clean the generated binaries if exist
    * @param project the project for the binaries that we are looking to clean 
    * @return the clean output 0 if there wasn't an error
    * @see Project
    */
    public int cleanBinaries(Project project) throws BuildException;
    
    /*
    * Create a Docker Image for the given project
    * @return the output of the execution, if 0 the docker image was created
    * @see Project
    */
    public int createDockerImage(Project project) throws BuildException;
}
