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

package org.uberfire.provisioning.services.endpoint.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.uberfire.provisioning.build.Binary;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.MavenBinary;
import org.uberfire.provisioning.docker.runtime.provider.DockerBinary;
import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.services.endpoint.api.BuildService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

/**
 * @author salaboy
 */
@ApplicationScoped
public class BuildServiceImpl implements BuildService {

    @Context
    private SecurityContext context;

    @Inject
    private Build build;

    @Inject
    private BuildRegistry registry;

    public BuildServiceImpl() {

    }

    @PostConstruct
    public void init() {
        System.out.println( "Post Construct Build ServiceImpl here!" );
    }

    @Override
    public List<Binary> getAllBinaries() throws BusinessException {
        return registry.getAllBinaries();
    }

    @Override
    public String newBuild( Project project ) throws BusinessException {
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
            Logger.getLogger( BuildServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Build Failed: " + ex.getMessage(), ex );
        }
    }

    @Override
    public String createDockerImage( Project project ) throws BusinessException {
        try {
            //
            int result = build.createDockerImage( project );
            if ( result != 0 ) {
                throw new BusinessException( "Building Docker image failed with code: " + result );
            }

            Binary binary = new DockerBinary( project );

            registry.registerBinary( binary );

            return build.binariesPath( project ).toUri().toString();

        } catch ( BuildException ex ) {
            Logger.getLogger( BuildServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Build Failed: " + ex.getMessage(), ex );
        }
    }

}
