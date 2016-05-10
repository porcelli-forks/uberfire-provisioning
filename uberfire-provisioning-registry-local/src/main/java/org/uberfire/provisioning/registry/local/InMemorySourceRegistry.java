/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Repository;

/**
 *
 * @author salaboy This is a not thread-safe implementation for local testing. A
 * more robust and distributed implementation should be provided for real usecases
 */
public class InMemorySourceRegistry implements SourceRegistry {

    private final Map<String, Repository> repositorySourcesLocation;
    //Store the repository name and location for reverse lookup
    private final Map<String, String> locationByRepositoryId;

    public InMemorySourceRegistry() {
        repositorySourcesLocation = new HashMap<>();
        locationByRepositoryId = new HashMap<>();
    }

    @Override
    public void registerRepositorySources(String location, Repository repo) {
        repositorySourcesLocation.put(location, repo);
        locationByRepositoryId.put(repo.getId(), location);
    }

    @Override
    public String getRepositoryLocation(Repository repo) {
        return locationByRepositoryId.get(repo.getId());
    }
    
    @Override
    public String getRepositoryLocationById(String repoId) {
        return locationByRepositoryId.get(repoId);
    }

    @Override
    public Repository getRepositoryByLocation(String location) {
        return repositorySourcesLocation.get(location);
    }

    @Override
    public List<Repository> getAllRepositories() {
        return new ArrayList<>(repositorySourcesLocation.values());
    }

    
}
