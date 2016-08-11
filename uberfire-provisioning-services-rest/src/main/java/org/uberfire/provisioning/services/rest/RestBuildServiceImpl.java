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

package org.uberfire.provisioning.services.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.uberfire.provisioning.build.Binary;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.model.MavenBinary;
import org.uberfire.provisioning.docker.runtime.provider.DockerBinary;
import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.services.api.BuildService;
import org.uberfire.provisioning.services.exceptions.BusinessException;

@ApplicationScoped
public class RestBuildServiceImpl implements BuildService {

    @Inject
    private Build build;

    @Inject
    private BuildRegistry registry;

    @PostConstruct
    public void init() {

    }

    @Override
    public List<Binary> getAllBinaries() throws BusinessException {
        return registry.getAllBinaries();
    }

    @Override
    public String newBuild( final Project project ) throws BusinessException {
        try {
            //
            int result = build.build( project );
            if ( result != 0 ) {
                throw new BusinessException( "Build Failed with code: " + result );
            }

            Binary binary = new MavenBinary( project );

            registry.registerBinary( binary );

            return build.binariesPath( project ).toUri().toString();

        } catch ( BuildException ex ) {
            Logger.getLogger( RestBuildServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Build Failed: " + ex.getMessage(), ex );
        }
    }

    @Override
    public String createDockerImage( final Project project,
                                     boolean push,
                                     String username,
                                     String password ) throws BusinessException {
        try {
            int result = build.createDockerImage( project, push, username, password );
            if ( result != 0 ) {
                throw new BusinessException( "Building Docker image failed with code: " + result );
            }

            Binary binary = new DockerBinary( project );

            registry.registerBinary( binary );

            return build.binariesPath( project ).toUri().toString();

        } catch ( BuildException ex ) {
            Logger.getLogger( RestBuildServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Build Failed: " + ex.getMessage(), ex );
        }
    }

}
