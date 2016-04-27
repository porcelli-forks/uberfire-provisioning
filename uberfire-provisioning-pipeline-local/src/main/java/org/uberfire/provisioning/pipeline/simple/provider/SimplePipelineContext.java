/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.simple.provider;

import java.util.HashMap;
import java.util.Map;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;

/**
 *
 * @author salaboy
 */
public class SimplePipelineContext implements PipelineContext {

    private Map<String, Object> context = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getData() {
        return context;
    }

}
