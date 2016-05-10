/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.uberfire.provisioning.build.spi.exceptions.BuildException;

/**
 *
 * @author salaboy
 */
@JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_OBJECT)
public interface Build {

    public int build(Project project) throws BuildException;

    public boolean binariesReady(Project project) throws BuildException;

    public String binariesLocation(Project project);
}
