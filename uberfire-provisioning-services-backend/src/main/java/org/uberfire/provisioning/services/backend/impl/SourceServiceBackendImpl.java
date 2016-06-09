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

package org.uberfire.provisioning.services.backend.impl;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.services.api.SourceService;
import org.uberfire.provisioning.services.api.backend.SourceServiceBackend;
import org.uberfire.provisioning.services.exceptions.BusinessException;
import org.uberfire.provisioning.source.Repository;

@Service
@ApplicationScoped
public class SourceServiceBackendImpl implements SourceServiceBackend {

    private SourceService sourceService;

    public SourceServiceBackendImpl() {

    }

    @Inject
    public SourceServiceBackendImpl( final SourceService sourceService ) {
        this.sourceService = sourceService;
    }

    @Override
    public List<Repository> getAllRepositories() throws BusinessException {
        return sourceService.getAllRepositories();
    }

    @Override
    public String getPathByRepositoryId( final String repositoryId ) throws BusinessException {
        return sourceService.getPathByRepositoryId( repositoryId );
    }

    @Override
    public String registerRepository( final Repository repo ) throws BusinessException {
        return sourceService.registerRepository( repo );
    }

    @Override
    public void registerProject( final String repositoryId,
                                 final Project project ) throws BusinessException {
        sourceService.registerProject( repositoryId, project );
    }

    @Override
    public List<Project> getAllProjects( final String repositoryId ) throws BusinessException {
        return sourceService.getAllProjects( repositoryId );
    }

}
