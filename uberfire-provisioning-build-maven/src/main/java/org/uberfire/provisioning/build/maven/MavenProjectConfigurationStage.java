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

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;

public class MavenProjectConfigurationStage implements Stage {

    @Override
    public String getName() {
        return "Configuring Maven Project";
    }

    @Override
    public void execute( PipelineContext context ) {
        Source source = ( Source ) context.getData().get( "source" );
        String projectName = ( String ) context.getData().get( "projectName" );
        String expectedBinary = ( String ) context.getData().get( "expectedBinary" );
        Repository repository = ( Repository ) context.getData().get( "repository" );
        SourceRegistry sourceRegistry = ( SourceRegistry ) context.getServices().get( "sourceRegistry" );

        Project project = new MavenProject( projectName, expectedBinary, source.getPath().resolve( projectName ), source.getPath() );

        sourceRegistry.registerProject( repository, project );

        context.getData().put( "project", project );
    }

}
