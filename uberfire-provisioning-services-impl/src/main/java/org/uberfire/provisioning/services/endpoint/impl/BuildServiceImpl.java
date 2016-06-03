package org.uberfire.provisioning.services.endpoint.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.build.maven.MavenBinary;
import org.uberfire.provisioning.build.spi.Binary;
import org.uberfire.provisioning.build.spi.Build;
import org.uberfire.provisioning.build.spi.Project;
import org.uberfire.provisioning.build.spi.exceptions.BuildException;
import org.uberfire.provisioning.docker.runtime.provider.DockerBinary;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.services.endpoint.api.BuildService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

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
    private BuildRegistry registry;

    public BuildServiceImpl() {

    }

    @PostConstruct
    public void init() {
        System.out.println("Post Construct Build ServiceImpl here!");
    }

    @Override
    public List<Binary> getAllBinaries() throws BusinessException {
        return registry.getAllBinaries();
    }

    @Override
    public String newBuild(Project project) throws BusinessException {
        try {
            //
            int result = build.build(project);
            if(result != 0){
                throw new BusinessException("Build Failed with code: "+ result);
            }
            
            Binary binary = new MavenBinary(project);
            
            registry.registerBinary(binary);
            
            return build.binariesLocation(project);
            
        } catch (BuildException ex) {
            Logger.getLogger(BuildServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new BusinessException("Build Failed: "+ ex.getMessage(), ex);
        }
    }

    @Override
    public String createDockerImage(Project project) throws BusinessException {
        try {
            //
            int result = build.createDockerImage(project);
            if(result != 0){
                throw new BusinessException("Building Docker image failed with code: "+ result);
            }
            
            Binary binary = new DockerBinary(project);
            
            registry.registerBinary(binary);
            
            return build.binariesLocation(project);
            
        } catch (BuildException ex) {
            Logger.getLogger(BuildServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new BusinessException("Build Failed: "+ ex.getMessage(), ex);
        }
    }
    
    
    

}
