/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;

/**
 *
 * @author salaboy
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT)
public interface ProviderConfiguration {

    public Map<String, String> getProperties();

    public void setProperties(Map<String, String> props);
    
    public String getName();
    
    public void setName(String name);
    
    public String getProvider();
    
    public void setProvider(String name);
}
