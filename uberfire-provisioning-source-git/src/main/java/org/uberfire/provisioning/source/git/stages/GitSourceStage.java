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

package org.uberfire.provisioning.source.git.stages;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;

import org.uberfire.java.nio.file.FileSystem;
import org.uberfire.java.nio.file.FileSystems;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.source.git.GitRepository;
import org.uberfire.provisioning.source.git.UFLocal;

public class GitSourceStage implements Stage {

    @Override
    public String getName() {
        return "Git Source Stage";
    }

    @Override
    public void execute( PipelineContext context ) {

        String repository = (String) context.getData().get( "repository" );
        String branch = (String) context.getData().get( "branch" );
        String origin = (String) context.getData().get( "origin" );
        String uriString = (String) context.getData().get( "uri" );
        File path = (File) context.getData().get( "path" );

        final URI uri = URI.create( uriString );
        final FileSystem fs = FileSystems.newFileSystem( uri, new HashMap<String, Object>() {
            {
                put( "init", Boolean.TRUE );
                put( "origin", origin );
                put( "out-dir", path.getAbsolutePath() );
            }
        } );

        final UFLocal local = new UFLocal();
        final GitRepository gitRepository = (GitRepository) local.getRepository( repository, Collections.emptyMap() );
        final Source source = gitRepository.getSource( branch );

        SourceRegistry sourceRegistry = (SourceRegistry) context.getServices().get( "sourceRegistry" );
        sourceRegistry.registerRepositorySources( source.getPath(), gitRepository );
        //Setting source and repo to be used by another stage
        context.getData().put( "source", source );
        context.getData().put( "repository", gitRepository );

    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Stage ) ) {
            return false;
        }

        final Stage that = (Stage) o;

        return getName() != null ? getName().equals( that.getName() ) : that.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

}
