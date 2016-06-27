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

public class MavenBuildStage implements Stage {

    private final String name;

    public MavenBuildStage() {
        this.name = "Maven Build Stage";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void execute( PipelineContext context ) {
        Project project = (Project) context.getData().get( "project" );
        Build build = (Build) context.getServices().get( "buildService" );

        int result = 0;
        try {
            result = build.build( project );
        } catch ( BuildException ex ) {
            Logger.getLogger( MavenBuildStage.class.getName() ).log( Level.SEVERE, null, ex );
        }
        if ( result != 0 ) {
            throw new IllegalStateException( "Maven Build Failed! Review Logs for errors" );
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
