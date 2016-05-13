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
import org.uberfire.provisioning.build.spi.Project;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Repository;

/**
 *
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

    @Override
    public void registerProject(Repository repo, Project project) {
        if (projectsByRepo.get(repo) == null) {
            projectsByRepo.put(repo, new ArrayList<>());
        }
        projectsByRepo.get(repo).add(project);
    }

    @Override
    public List<Project> getAllProjects(Repository repository) {
        List<Project> allProjects = new ArrayList<>();
        for (Repository r : projectsByRepo.keySet()) {
            allProjects.addAll(projectsByRepo.get(r));
        }
        return allProjects;
    }

    @Override
    public List<Project> getProjectByName(String projectName) {
        List<Project> projectsByName = new ArrayList<>();
        // Nasty Lookup, fix and improve this for distributed implementation
        for (Repository r : projectsByRepo.keySet()) {
            for (Project p : projectsByRepo.get(r)) {
                if (p.getName().equals(projectName)) {
                    projectsByName.add(p);
                }
            }
        }
        return projectsByName;
    }

    @Override
    public Repository getRepositoryById(String repositoryId) {
        String repoLocation = locationByRepositoryId.get(repositoryId);
        return repositorySourcesLocation.get(repoLocation);

    }

}
