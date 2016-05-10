/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import java.util.UUID;
import org.uberfire.provisioning.build.spi.Project;

/**
 *
 * @author salaboy
 */
public class MavenProject implements Project {

    private String id;
    private String name;
    private String type;
    private String rootPath;
    private String path;
    private String expectedBinary;

    public MavenProject() {
        this.id = UUID.randomUUID().toString().substring(0, 12);
        this.type = "Maven";
    }

    public MavenProject(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
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
    public String getPath() {
        return path;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getExpectedBinary() {
        return expectedBinary;
    }

    @Override
    public void setExpectedBinary(String expectedBinary) {
        this.expectedBinary = expectedBinary;
    }

}
