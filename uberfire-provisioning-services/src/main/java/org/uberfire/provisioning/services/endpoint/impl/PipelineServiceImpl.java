package org.uberfire.provisioning.services.endpoint.impl;

import com.spotify.docker.client.shaded.javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.pipeline.simple.provider.PrintOutStage;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipeline;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineContext;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineInstance;
import org.uberfire.provisioning.pipeline.spi.Pipeline;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;
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

    public PipelineServiceImpl() {

    }

    @PostConstruct
    public void init() {
        SimplePipeline simplePipeline = new SimplePipeline("my pipe");
        simplePipeline.addStage(new PrintOutStage("Simple PrintOut Stage"));
        simplePipeline.addStage(new PrintOutStage("Simple PrintOut 2 Stage"));
        pipelines.put(simplePipeline.getId(), simplePipeline);
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
    public void runPipeline(String id) throws BusinessException {
        PipelineContext simplePipelineContext = new SimplePipelineContext();
        simplePipelineContext.getData().put("message", " -> print out stage is printing this");

        new SimplePipelineInstance(pipelines.get(id)).run(simplePipelineContext);
    }

}
