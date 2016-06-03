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

package org.uberfire.provisioning.registry;

import java.util.List;

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.source.Repository;

/**
 * @author salaboy
 */
public interface SourceRegistry {

    void registerRepositorySources( String location,
                                    Repository repo );

    String getRepositoryLocation( Repository repo );

    Repository getRepositoryByLocation( String location );

    List<Repository> getAllRepositories();

    String getRepositoryLocationById( String repoId );

    void registerProject( Repository repo,
                          Project project );

    List<Project> getAllProjects( Repository repo );

    List<Project> getProjectByName( String usersnew );

    Repository getRepositoryById( String repositoryId );

}
