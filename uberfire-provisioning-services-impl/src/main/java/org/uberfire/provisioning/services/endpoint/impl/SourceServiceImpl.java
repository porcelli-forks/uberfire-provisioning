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

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.exceptions.SourcingException;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.services.endpoint.api.SourceService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;

/**
 * @author salaboy
 */
@ApplicationScoped
public class SourceServiceImpl implements SourceService {

    @Context
    private SecurityContext context;

    @Inject
    private Source source;

    @Inject
    private SourceRegistry registry;

    public SourceServiceImpl() {

    }

    @PostConstruct
    public void init() {
        System.out.println( "Post Construct Source ServiceImpl here!" );
    }

    @Override
    public List<Repository> getAllRepositories() throws BusinessException {
        return registry.getAllRepositories();
    }

    @Override
    public String getLocationByRepositoryId( String repositoryId ) throws BusinessException {
        return registry.getRepositoryLocationById( repositoryId );
    }

    @Override
    public String registerRepository( Repository repo ) throws BusinessException {
        try {
            String sourceDir = source.getSource( repo );
            registry.registerRepositorySources( sourceDir, repo );
            return repo.getId();
        } catch ( SourcingException ex ) {
            Logger.getLogger( SourceServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( ex.getMessage(), ex );
        }

    }

    @Override
    public void registerProject( String repositoryId,
                                 Project project ) throws BusinessException {
        Repository repo = registry.getRepositoryById( repositoryId );
        registry.registerProject( repo, project );
    }

    @Override
    public List<Project> getAllProjects( String repositoryId ) throws BusinessException {
        Repository repo = registry.getRepositoryById( repositoryId );
        return registry.getAllProjects( repo );
    }

}
