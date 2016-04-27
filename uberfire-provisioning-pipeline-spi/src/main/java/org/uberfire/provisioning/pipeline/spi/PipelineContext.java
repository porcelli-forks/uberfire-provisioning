/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.spi;

import java.util.Map;

/**
 *
 * @author salaboy
 */
public interface PipelineContext {
    Map<String, Object> getData();
}
