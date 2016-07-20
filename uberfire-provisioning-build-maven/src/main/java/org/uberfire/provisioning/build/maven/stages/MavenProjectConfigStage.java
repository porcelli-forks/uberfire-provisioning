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

package org.uberfire.provisioning.build.maven.stages;

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.MavenProject;
import org.uberfire.provisioning.build.maven.RepositoryVisitor;
import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

public class MavenProjectConfigStage extends BaseStage {

    private Source source;
    private String projectName;
    private String expectedBinary;

    // In Holders
    private String sourceHolder;
    // Out Holders
    private String warPathHolder;
    private String projectHolder;

    public MavenProjectConfigStage() {
        addRequiredService( SourceRegistry.class );
    }

    public Source getSource() {
        return source;
    }

    public void setSource( Source source ) {
        this.source = source;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName( String projectName ) {
        this.projectName = projectName;
    }

    public String getExpectedBinary() {
        return expectedBinary;
    }

    public void setExpectedBinary( String expectedBinary ) {
        this.expectedBinary = expectedBinary;
    }

    public String getWarPathHolder() {
        return warPathHolder;
    }

    public void setWarPathHolder( String warPathHolder ) {
        this.warPathHolder = warPathHolder;
    }

    public String getProjectHolder() {
        return projectHolder;
    }

    public void setProjectHolder( String projectHolder ) {
        this.projectHolder = projectHolder;
    }

    public String getSourceHolder() {
        return sourceHolder;
    }

    public void setSourceHolder( String sourceHolder ) {
        this.sourceHolder = sourceHolder;
    }

    @Override
    public void execute( PipelineInstance pipe, PipelineDataContext pipeData ) {
        if ( getSource() == null && getSourceHolder() != null ) {
            setSource( ( Source ) pipeData.getData( getSourceHolder() ) );
        }
        Project project = new MavenProject( getProjectName(), getExpectedBinary(), getSource().getPath().resolve( getProjectName() ), getSource().getPath() );

        pipeData.setData( "${" + getProjectHolder() + "}", project );
        RepositoryVisitor repositoryVisitor = new RepositoryVisitor( project );
        pipeData.setData( "${" + getWarPathHolder() + "}", repositoryVisitor.getTargetFolder().toString() + "/" + getExpectedBinary() );
    }

    public static MavenProjectConfigStageBuilder builder() {
        return new MavenProjectConfigStageBuilder();

    }

    public static class MavenProjectConfigStageBuilder extends BaseStageBuilder<MavenProjectConfigStage> {

        private MavenProjectConfigStageBuilder() {
            stage = new MavenProjectConfigStage();
        }

        @Override
        public MavenProjectConfigStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public MavenProjectConfigStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public MavenProjectConfigStageBuilder withExpectedBinary( String expectedBinary ) {
            stage.setExpectedBinary( expectedBinary );
            return this;
        }

        public MavenProjectConfigStageBuilder withProjectName( String projectName ) {
            stage.setProjectName( projectName );
            return this;
        }

        public MavenProjectConfigStageBuilder withSource( Source source ) {
            stage.setSource( source );
            return this;
        }

        public MavenProjectConfigStageBuilder outProject( String projectHolder ) {
            stage.setProjectHolder( projectHolder );
            return this;
        }

        public MavenProjectConfigStageBuilder outWarPath( String warPathHolder ) {
            stage.setWarPathHolder( warPathHolder );
            return this;
        }

        public MavenProjectConfigStageBuilder inSource( String sourceHolder ) {
            stage.setSourceHolder( sourceHolder );
            return this;
        }

    }
}
