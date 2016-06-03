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

package org.uberfire.provisioning.source.github;

import java.io.File;
import java.io.IOException;

import org.uberfire.provisioning.exceptions.SourcingException;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;

import static java.lang.System.*;
import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;
import static org.eclipse.jgit.api.Git.*;
import static org.eclipse.jgit.util.FileUtils.*;

/**
 * @author salaboy
 *         Git implementation for the Source interface using jgit
 */
public class GitSource implements Source {

    @Override
    public String getSource( Repository repository ) throws SourcingException {
        String tmpDir = getProperty( "java.io.tmpdir" );

        try {
            File createdTempDir = createTempDir( "uf-source", "", new File( tmpDir ) );
            cloneRepository()
                    .setURI( repository.getURI() )
                    .setDirectory( createdTempDir )
                    .setBranch( ( repository.getBranch() != null ) ? repository.getBranch() : "master" )
                    .call();

            return createdTempDir.getCanonicalPath();
        } catch ( Exception ex ) {
            getLogger( GitSource.class.getName() ).log( SEVERE, null, ex );
            throw new SourcingException( "Error Cloning Git Repository", ex );
        }
    }

    @Override
    public boolean cleanSource( String sourcePath ) throws SourcingException {
        try {
            delete( new File( sourcePath ), RECURSIVE );
            return true;
        } catch ( IOException ex ) {
            getLogger( GitSource.class.getName() ).log( SEVERE, null, ex );
            return false;
        }
    }

}
