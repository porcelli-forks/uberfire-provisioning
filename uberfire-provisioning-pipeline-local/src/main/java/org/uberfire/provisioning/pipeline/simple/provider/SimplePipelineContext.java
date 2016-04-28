/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.simple.provider;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class SimplePipelineContext implements PipelineContext {

    private Map<String, Object> context = new HashMap<String, Object>();
    
    @XmlTransient
    private Map<String, Object> services = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getData() {
        return context;
    }

    @Override
    public Map<String, Object> getServices() {
        return services;
    }
    
    

}
