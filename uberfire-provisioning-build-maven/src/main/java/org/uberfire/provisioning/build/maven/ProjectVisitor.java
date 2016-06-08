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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.uberfire.java.nio.file.DirectoryStream;
import org.uberfire.java.nio.file.Files;
import org.uberfire.java.nio.file.Path;
import org.uberfire.provisioning.build.Project;

public class ProjectVisitor {

    private Project project;
    private File rootFolder;
    private String buildRoot;

    public ProjectVisitor( final Project project ) {
        this.project = project;
        this.buildRoot = System.getProperty( "java.io.tmpdir" ) + File.separatorChar + "guvnor" + File.separatorChar + project.getName();
        Path path = this.project.getRootPath();
        makeTempRootDirectory();
        makeTempDirectory( path );
        rootFolder = makeTempDirectory( path );
        try {
            visitPaths( Files.newDirectoryStream( path ) );
        } catch ( IOException ex ){
            throw new RuntimeException( ex );
        }
    }

    public File getRootFolder() {
        return rootFolder;
    }

    public File getGuvnorTempFolder() {
        return new File( System.getProperty( "java.io.tmpdir" ) + File.separatorChar + "guvnor" );
    }

    public File getTargetFolder() {
        return new File( buildRoot + File.separatorChar + "target" );
    }

    private void visitPaths( final DirectoryStream<Path> directoryStream ) throws IOException {
        for ( final org.uberfire.java.nio.file.Path path : directoryStream ) {
            if ( Files.isDirectory( path ) ) {
                makeTempDirectory( path );
                visitPaths( Files.newDirectoryStream( path ) );
            } else {
                makeTempFile( path );
            }
        }
    }

    private File makeTempDirectory( Path path ) {
        return makeTempDirectory( getFilePath( path ) );
    }

    private File makeTempDirectory( String filePath ) {
        File tempDirectory = new File( filePath );
        if ( !tempDirectory.isFile() ) {
            tempDirectory.mkdir();
        }
        return tempDirectory;
    }

    private void makeTempRootDirectory() {
        File tempDirectory = new File( buildRoot );
        tempDirectory.mkdirs();
    }

    private void makeTempFile( Path path ) throws IOException {

        final int BUFFER = 2048;
        byte data[] = new byte[ BUFFER ];

        BufferedInputStream origin = new BufferedInputStream( Files.newInputStream( path ), BUFFER );

        String filePath = getFilePath( path );
        File tempFile = new File( filePath );
        tempFile.createNewFile();

        FileOutputStream output = new FileOutputStream( tempFile );

        int count;
        while ( ( count = origin.read( data, 0, BUFFER ) ) != -1 ) {
            output.write( data, 0, count );
        }

        origin.close();
        output.close();
    }

    private String getFilePath( Path path ) {
        return buildRoot + path.toUri().getRawPath();
    }
}