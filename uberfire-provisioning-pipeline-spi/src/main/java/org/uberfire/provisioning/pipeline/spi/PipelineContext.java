/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;

/**
 *
 * @author salaboy
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT)
public interface PipelineContext {
    Map<String, Object> getData();
    Map<String, Object> getServices();
}
