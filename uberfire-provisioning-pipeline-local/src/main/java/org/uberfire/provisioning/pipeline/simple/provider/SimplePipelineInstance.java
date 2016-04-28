/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.simple.provider;

import org.uberfire.provisioning.pipeline.spi.Pipeline;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;
import org.uberfire.provisioning.pipeline.spi.PipelineInstance;
import org.uberfire.provisioning.pipeline.spi.Stage;

/**
 *
 * @author salaboy
 */
public class SimplePipelineInstance implements PipelineInstance {

    private final Pipeline pipeline;

    public SimplePipelineInstance(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void run(PipelineContext context) {
        System.out.println(" >> Running Pipeline: " + pipeline.getName());
        
        for (Stage s : pipeline.getStages()) {
            System.out.println(" Executing Stage: " + s.getName());
            s.execute(context);
        }
    }
}
