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

import org.uberfire.provisioning.build.Project;

import static java.util.UUID.*;

/**
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
        this.id = randomUUID().toString().substring( 0, 12 );
        this.type = "Maven";
    }

    public MavenProject( String name ) {
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
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public void setPath( String path ) {
        this.path = path;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath( String rootPath ) {
        this.rootPath = rootPath;
    }

    @Override
    public String getExpectedBinary() {
        return expectedBinary;
    }

    @Override
    public void setExpectedBinary( String expectedBinary ) {
        this.expectedBinary = expectedBinary;
    }

}
