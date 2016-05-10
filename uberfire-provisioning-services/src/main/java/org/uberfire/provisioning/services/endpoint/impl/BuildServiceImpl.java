package org.uberfire.provisioning.services.endpoint.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.build.spi.Build;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.services.endpoint.api.BuildService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.exceptions.SourcingException;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class BuildServiceImpl implements BuildService {

    @Context
    private SecurityContext context;

    @Inject
    private Build build;

    @Inject
    private SourceRegistry registry;

    public BuildServiceImpl() {

    }

    @PostConstruct
    public void init() {
        System.out.println("Post Construct Build ServiceImpl here!");
    }

    @Override
    public List<Build> getAllBuilds() throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String newBuild(Build build) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
   

}
