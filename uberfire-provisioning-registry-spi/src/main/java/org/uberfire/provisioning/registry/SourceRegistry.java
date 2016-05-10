/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry;

import java.util.List;
import org.uberfire.provisioning.source.Repository;

/**
 *
 * @author salaboy
 */
public interface SourceRegistry {

    public void registerRepositorySources(String location, Repository repo);

    public String getRepositoryLocation(Repository repo);

    public Repository getRepositoryByLocation(String location);

    public List<Repository> getAllRepositories();

    public String getRepositoryLocationById(String repoId);
    

}
