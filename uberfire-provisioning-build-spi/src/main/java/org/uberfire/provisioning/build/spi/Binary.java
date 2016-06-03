/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author salaboy
 * Generic Binary type used to store information about the generated binaries.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface Binary {

    public Project getSourceProject();

    public String getLocation();

    public String getType();

    public String getName();

    public void setLocation(String location);

    public void setType(String type);

    public void setName(String name);

    public void setSourceProject(Project sourceProject);
}
