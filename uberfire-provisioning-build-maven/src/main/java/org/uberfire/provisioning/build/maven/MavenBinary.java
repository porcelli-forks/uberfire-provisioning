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

import org.uberfire.provisioning.build.Binary;
import org.uberfire.provisioning.build.Project;

/**
 * @author salaboy
 */
public class MavenBinary implements Binary {

    private String location;
    private String type;
    private String name;
    private Project sourceProject;

    public MavenBinary( Project sourceProject ) {
        this.sourceProject = sourceProject;
        this.location = sourceProject.getRootPath() + "/" + sourceProject.getPath() + "/target/" + sourceProject.getExpectedBinary();
        this.name = sourceProject.getExpectedBinary();
        this.type = "Maven";
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
    public void setLocation( String location ) {
        this.location = location;
    }

    @Override
    public void setType( String type ) {
        this.type = type;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public void setSourceProject( Project sourceProject ) {
        this.sourceProject = sourceProject;
    }

}
