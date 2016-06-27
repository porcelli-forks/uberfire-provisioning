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
import org.uberfire.provisioning.build.Binary;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.services.api.BuildService;
import org.uberfire.provisioning.services.api.backend.BuildServiceBackend;
import org.uberfire.provisioning.services.exceptions.BusinessException;

@Service
@ApplicationScoped
public class BuildServiceBackendImpl implements BuildServiceBackend {

    private BuildService buildService;

    public BuildServiceBackendImpl() {
    }

    @Inject
    public BuildServiceBackendImpl( final BuildService buildService ) {
        this.buildService = buildService;
    }

    @Override
    public List<Binary> getAllBinaries() throws BusinessException {
        return buildService.getAllBinaries();
    }

    @Override
    public String newBuild( final Project project ) throws BusinessException {
        return buildService.newBuild( project );
    }

    @Override
    public String createDockerImage( final Project project, boolean push, String username, String password ) throws BusinessException {
        return buildService.createDockerImage( project, push, username, password );
    }
}
