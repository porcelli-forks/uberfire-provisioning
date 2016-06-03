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

import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;

import static java.lang.System.*;
import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;

/**
 * @author salaboy
 */
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
        try {
            int buildResult = 0;
            MavenBuild build = new MavenBuild();
            MavenProject mavenProject = new MavenProject( (String) context.getData().get( "projectName" ) );
            mavenProject.setRootPath( (String) context.getData().get( "projectPath" ) );
            mavenProject.setExpectedBinary( (String) context.getData().get( "expectedBinary" ) );
            try {

                buildResult = build.build( mavenProject );
            } catch ( BuildException ex ) {
                getLogger( MavenBuildStage.class.getName() ).log( SEVERE, null, ex );
                return;
            }

            if ( buildResult != 0 ) {
                out.println( " >>> Build Failed! " );
                return;
            }
            String binaryLocation = build.binariesLocation( mavenProject );
            context.getData().put( "warPath", binaryLocation );

        } catch ( BuildException ex ) {
            getLogger( MavenBuildStage.class.getName() ).log( SEVERE, null, ex );
        }

    }

}
