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
import java.util.Collections;
import java.util.HashMap;
import java.net.URI;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.source.git.GitRepository;
import org.uberfire.provisioning.source.git.UFLocal;
import org.uberfire.java.nio.file.FileSystems;
import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

public class GitSourceStage extends BaseStage {

    private String repository;
    private String branch;
    private String origin;
    private String uriString;
    private File path;

    // Out Variables
    private String sourceHolder;

    public GitSourceStage() {
        addRequiredService( SourceRegistry.class );
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository( String repository ) {
        this.repository = repository;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch( String branch ) {
        this.branch = branch;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin( String origin ) {
        this.origin = origin;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString( String uriString ) {
        this.uriString = uriString;
    }

    public File getPath() {
        return path;
    }

    public void setPath( File path ) {
        this.path = path;
    }

    public String getSourceHolder() {
        return sourceHolder;
    }

    public void setSourceHolder( String sourceHolder ) {
        this.sourceHolder = sourceHolder;
    }

    @Override
    public void execute( PipelineInstance pipe, PipelineDataContext results ) {

        final URI uri = URI.create( uriString );
        FileSystems.newFileSystem( uri, new HashMap<String, Object>() {
            {
                put( "init", Boolean.TRUE );
                put( "origin", origin );
                put( "out-dir", path.getAbsolutePath() );
            }
        } );

        final UFLocal local = new UFLocal();
        final GitRepository gitRepository = ( GitRepository ) local.getRepository( repository, Collections.emptyMap() );
        final Source source = gitRepository.getSource( branch );

        SourceRegistry sourceRegistry = pipe.getService( SourceRegistry.class );
        sourceRegistry.registerRepositorySources( source.getPath(), gitRepository );
        //Setting source to be used by another stage
        results.setData( "${" + getSourceHolder() + "}", source );
    }

    public static GitSourceStageBuilder builder() {
        return new GitSourceStageBuilder();

    }

    public static class GitSourceStageBuilder extends BaseStageBuilder<GitSourceStage> {

        private GitSourceStageBuilder() {
            stage = new GitSourceStage();
        }

        @Override
        public GitSourceStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public GitSourceStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public GitSourceStageBuilder withRepository( String repository ) {
            stage.setRepository( repository );
            return this;
        }

        public GitSourceStageBuilder withOrigin( String origin ) {
            stage.setOrigin( origin );
            return this;
        }

        public GitSourceStageBuilder withPath( File path ) {
            stage.setPath( path );
            return this;
        }

        public GitSourceStageBuilder withURI( String uriString ) {
            stage.setUriString( uriString );
            return this;
        }

        public GitSourceStageBuilder withBranch( String branch ) {
            stage.setBranch( branch );
            return this;
        }

        public GitSourceStageBuilder outSource( String sourceHolder ) {
            stage.setSourceHolder( sourceHolder );
            return this;
        }

    }

}
