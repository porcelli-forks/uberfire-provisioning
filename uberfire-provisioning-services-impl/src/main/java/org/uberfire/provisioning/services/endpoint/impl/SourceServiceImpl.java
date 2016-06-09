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
import javax.inject.Inject;

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.services.endpoint.api.SourceService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.source.Repository;

public class SourceServiceImpl implements SourceService {

    @Inject
    private SourceRegistry registry;

    @PostConstruct
    public void init() {
        System.out.println( "Post Construct Source ServiceImpl here!" );
    }

    @Override
    public List<Repository> getAllRepositories() throws BusinessException {
        return registry.getAllRepositories();
    }

    @Override
    public String getPathByRepositoryId( final String repositoryId ) throws BusinessException {
        return registry.getRepositoryPathById( repositoryId ).toUri().toString();
    }

    @Override
    public String registerRepository( final Repository repo ) throws BusinessException {
        try {
            registry.registerRepositorySources( repo.getSource().getPath(), repo );
            return repo.getId();
        } catch ( Exception ex ) {
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
