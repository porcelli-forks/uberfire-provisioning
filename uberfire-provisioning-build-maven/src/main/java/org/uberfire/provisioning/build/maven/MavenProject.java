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

import java.math.BigInteger;
import java.nio.charset.Charset;

import org.uberfire.java.nio.file.Path;
import org.uberfire.provisioning.build.Project;

/**
 * @author salaboy
 */
public class MavenProject implements Project {

    private final String id;
    private final String name;
    private final String type;
    private final String expectedBinary;
    private final Path rootPath;
    private final Path path;

    public MavenProject( final String name,
                         final String expectedBinary,
                         final Path rootPath,
                         final Path path ) {
        this.id = toHex( name );
        this.name = name;
        this.type = "Maven";
        this.expectedBinary = expectedBinary;
        this.rootPath = rootPath;
        this.path = path;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getExpectedBinary() {
        return expectedBinary;
    }

    @Override
    public Path getBinaryPath() {
        return getRootPath().resolve( getPath() ).resolve( "target" ).resolve( getExpectedBinary() );
    }

    @Override
    public Path getRootPath() {
        return rootPath;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof MavenProject ) ) {
            return false;
        }

        final MavenProject that = (MavenProject) o;

        if ( !id.equals( that.id ) ) {
            return false;
        }
        if ( !name.equals( that.name ) ) {
            return false;
        }
        return type.equals( that.type );

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    private String toHex( String arg ) {
        return String.format( "%040x", new BigInteger( 1, arg.getBytes( Charset.forName( "UTF-8" ) ) ) );
    }

}
