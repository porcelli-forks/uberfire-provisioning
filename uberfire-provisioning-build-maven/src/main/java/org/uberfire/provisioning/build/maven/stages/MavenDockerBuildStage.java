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
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;

public class MavenDockerBuildStage implements Stage {

    private final String name;

    public MavenDockerBuildStage() {
        this.name = "Maven Docker Build Stage";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void execute( PipelineContext context ) {
        Project project = ( Project ) context.getData().get( "project" );
        String push = ( String ) context.getData().get( "push" );
        String username = ( String ) context.getData().get( "username" );
        String password = ( String ) context.getData().get( "password" );
        Build build = ( Build ) context.getServices().get( "buildService" );
        try {
            int result = build.createDockerImage(project, Boolean.valueOf( push ), username, password);
            System.out.println( "Build Result: " + result );
        } catch ( BuildException ex ) {
            Logger.getLogger(MavenDockerBuildStage.class.getName() ).log( Level.SEVERE, null, ex );
        }

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
