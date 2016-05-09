/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;

/**
 *
 * @author salaboy This class holds all the information about configuration used
 * to start a specific runtime
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT)
public interface RuntimeConfiguration {

    public String getProviderName();
    
    public void setProviderName(String providerName);
    
    public Map<String, String> getProperties();

    public void setProperties(Map<String, String> props);
    
    
}
