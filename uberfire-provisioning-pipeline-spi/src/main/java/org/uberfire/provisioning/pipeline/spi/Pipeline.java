/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.List;

/**
 *
 * @author salaboy
 */
@JsonTypeInfo(use=Id.CLASS, include=As.WRAPPER_OBJECT)
public interface Pipeline {
    String getId();
    String getName();
    void setId(String id);
    void setName(String name);
    void addStage(Stage stage);
    List<Stage> getStages();
    
}
