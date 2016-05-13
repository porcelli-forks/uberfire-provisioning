package org.uberfire.provisioning.services.endpoint.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.build.spi.Project;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.services.endpoint.api.SourceService;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.source.exceptions.SourcingException;

/**
 *
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
        System.out.println("Post Construct Source ServiceImpl here!");
    }

    @Override
    public List<Repository> getAllRepositories() throws BusinessException {
        return registry.getAllRepositories();
    }

    @Override
    public String getLocationByRepositoryId(String repositoryId) throws BusinessException {
        return registry.getRepositoryLocationById(repositoryId);
    }

    @Override
    public String registerRepository(Repository repo) throws BusinessException {
        try {
            String sourceDir = source.getSource(repo);
            registry.registerRepositorySources(sourceDir, repo);
            return sourceDir;
        } catch (SourcingException ex) {
            Logger.getLogger(SourceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new BusinessException(ex.getMessage(), ex);
        }

    }

    @Override
    public void registerProject(String repositoryId, Project project) throws BusinessException {
        Repository repo = registry.getRepositoryById(repositoryId);
        registry.registerProject(repo, project);
    }

    @Override
    public List<Project> getAllProjects(String repositoryId) throws BusinessException {
        Repository repo = registry.getRepositoryById(repositoryId);
        return registry.getAllProjects(repo);
    }

}
