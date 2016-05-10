/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import org.uberfire.provisioning.build.spi.Binary;
import org.uberfire.provisioning.build.spi.Project;

/**
 *
 * @author salaboy
 */
public class MavenBinary implements Binary {

    private String location;
    private String type;
    private String name;
    private Project sourceProject;

    public MavenBinary(Project sourceProject) {
        this.sourceProject = sourceProject;
        this.location = sourceProject.getRootPath() + "/" + sourceProject.getPath() + "/" + sourceProject.getExpectedBinary();
        this.name = sourceProject.getExpectedBinary();
    }

    @Override
    public Project getSourceProject() {
        return sourceProject;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setSourceProject(Project sourceProject) {
        this.sourceProject = sourceProject;
    }

}
