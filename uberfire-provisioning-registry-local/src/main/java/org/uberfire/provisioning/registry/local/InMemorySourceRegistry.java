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

package org.uberfire.provisioning.registry.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Repository;

/**
 * @author salaboy
 * @TODO: This is a not thread-safe implementation for local testing. A more
 * robust and distributed implementation should be provided for real use cases.
 * All the lookups mechanisms and structures needs to be improved for
 * performance.
 */
public class InMemorySourceRegistry implements SourceRegistry {

    private final Map<String, Repository> repositorySourcesLocation;
    //Store the repository id and location for reverse lookup
    private final Map<String, String> locationByRepositoryId;

    private final Map<Repository, List<Project>> projectsByRepo;

    public InMemorySourceRegistry() {
        repositorySourcesLocation = new HashMap<>();
        locationByRepositoryId = new HashMap<>();
        projectsByRepo = new HashMap<>();
    }

    @Override
    public void registerRepositorySources( String location,
                                           Repository repo ) {
        repositorySourcesLocation.put( location, repo );
        locationByRepositoryId.put( repo.getId(), location );
    }

    @Override
    public String getRepositoryLocation( Repository repo ) {
        return locationByRepositoryId.get( repo.getId() );
    }

    @Override
    public String getRepositoryLocationById( String repoId ) {
        return locationByRepositoryId.get( repoId );
    }

    @Override
    public Repository getRepositoryByLocation( String location ) {
        return repositorySourcesLocation.get( location );
    }

    @Override
    public List<Repository> getAllRepositories() {
        return new ArrayList<>( repositorySourcesLocation.values() );
    }

    @Override
    public void registerProject( Repository repo,
                                 Project project ) {
        if ( projectsByRepo.get( repo ) == null ) {
            projectsByRepo.put( repo, new ArrayList<>() );
        }
        projectsByRepo.get( repo ).add( project );
    }

    @Override
    public List<Project> getAllProjects( Repository repository ) {
        List<Project> allProjects = new ArrayList<>();
        for ( Repository r : projectsByRepo.keySet() ) {
            allProjects.addAll( projectsByRepo.get( r ) );
        }
        return allProjects;
    }

    @Override
    public List<Project> getProjectByName( String projectName ) {
        List<Project> projectsByName = new ArrayList<>();
        // Nasty Lookup, fix and improve this for distributed implementation
        for ( Repository r : projectsByRepo.keySet() ) {
            for ( Project p : projectsByRepo.get( r ) ) {
                if ( p.getName().equals( projectName ) ) {
                    projectsByName.add( p );
                }
            }
        }
        return projectsByName;
    }

    @Override
    public Repository getRepositoryById( String repositoryId ) {
        String repoLocation = locationByRepositoryId.get( repositoryId );
        return repositorySourcesLocation.get( repoLocation );

    }

}
