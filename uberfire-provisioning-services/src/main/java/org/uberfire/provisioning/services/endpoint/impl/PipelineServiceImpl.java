package org.uberfire.provisioning.services.endpoint.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineInstance;
import org.uberfire.provisioning.pipeline.spi.Pipeline;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;
import org.uberfire.provisioning.services.endpoint.api.ContainerProvisioningService;
import org.uberfire.provisioning.services.endpoint.api.PipelineService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class PipelineServiceImpl implements PipelineService {

    @Context
    private SecurityContext context;

    private Map<String, Pipeline> pipelines = new HashMap<String, Pipeline>();
    
    @Inject
    private ContainerProvisioningService provisioningService;

    public PipelineServiceImpl() {
        
    }

    @PostConstruct
    public void init() {
        System.out.println("Post Construct Pipeline ServiceImpl here!");
    }

    @Override
    public List<Pipeline> getAllPipelines() throws BusinessException {
        return new ArrayList<>(pipelines.values());
    }

    @Override
    public String newPipeline(Pipeline pipeline) throws BusinessException {
        String id = UUID.randomUUID().toString();
        pipeline.setId(id);
        pipelines.put(pipeline.getId(), pipeline);
        return pipeline.getId();
    }

    @Override
    public void runPipeline(String id, PipelineContext context) throws BusinessException {
        context.getServices().put("provisioningService", provisioningService);
        new SimplePipelineInstance(pipelines.get(id)).run(context);
    }

}
