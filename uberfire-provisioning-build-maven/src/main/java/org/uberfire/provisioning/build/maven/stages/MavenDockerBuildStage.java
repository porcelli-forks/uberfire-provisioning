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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

public class MavenDockerBuildStage extends BaseStage {

    private Project project;
    private String projectHolder;
    private String username;
    private String password;
    private Boolean push;

    private String pushHolder;
    private String usernameHolder;
    private String passwordHolder;

    public MavenDockerBuildStage() {
        addRequiredService( Build.class );
    }

    public Project getProject() {
        return project;
    }

    public void setProject( Project project ) {
        this.project = project;
    }

    public String getProjectHolder() {
        return projectHolder;
    }

    public void setProjectHolder( String projectHolder ) {
        this.projectHolder = projectHolder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public Boolean getPush() {
        return push;
    }

    public void setPush( Boolean push ) {
        this.push = push;
    }

    public String getPushHolder() {
        return pushHolder;
    }

    public void setPushHolder( String pushHolder ) {
        this.pushHolder = pushHolder;
    }

    public String getUsernameHolder() {
        return usernameHolder;
    }

    public void setUsernameHolder( String usernameHolder ) {
        this.usernameHolder = usernameHolder;
    }

    public String getPasswordHolder() {
        return passwordHolder;
    }

    public void setPasswordHolder( String passwordHolder ) {
        this.passwordHolder = passwordHolder;
    }

    @Override
    public void execute( PipelineInstance pipe, PipelineDataContext pipeData ) {
        if ( getProject() == null && getProjectHolder() != null ) {
            setProject( ( Project ) pipeData.getData( getProjectHolder() ) );
        }

        if ( getPush() == null && getPushHolder() != null ) {
            setPush( Boolean.getBoolean( ( String ) pipeData.getData( getPushHolder() ) ) );
        }

        if ( getUsername() == null && getUsernameHolder() != null ) {
            setUsername( ( String ) pipeData.getData( getUsernameHolder() ) );
        }

        if ( getPassword() == null && getPassword() != null ) {
            setPassword( ( String ) pipeData.getData( getPasswordHolder() ) );
        }

        int result = 0;
        try {
            result = pipe.getService( Build.class ).createDockerImage( getProject(), getPush(), getUsername(), getPassword() );
        } catch ( BuildException ex ) {
            Logger.getLogger(MavenDockerBuildStage.class.getName() ).log( Level.SEVERE, null, ex );
        }
        if ( result != 0 ) {
            throw new IllegalStateException( "Maven Build Failed! Review Logs for errors" );
        }
    }

    public static MavenBuildStageBuilder builder() {
        return new MavenBuildStageBuilder();

    }

    public static class MavenBuildStageBuilder extends BaseStageBuilder<MavenDockerBuildStage> {

        private MavenBuildStageBuilder() {
            stage = new MavenDockerBuildStage();
        }

        @Override
        public MavenBuildStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public MavenBuildStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public MavenBuildStageBuilder withProject( Project project ) {
            stage.setProject( project );
            return this;
        }

        public MavenBuildStageBuilder withUsername( String username ) {
            stage.setUsername( username );
            return this;
        }

        public MavenBuildStageBuilder withPassword( String password ) {
            stage.setPassword( password );
            return this;
        }

        public MavenBuildStageBuilder withPush( Boolean push ) {
            stage.setPush( push );
            return this;
        }

        public MavenBuildStageBuilder inProject( String projectHolder ) {
            stage.setProjectHolder( projectHolder );
            return this;
        }

        public MavenBuildStageBuilder inPush( String pushHolder ) {
            stage.setPushHolder( pushHolder );
            return this;
        }

    }
}
