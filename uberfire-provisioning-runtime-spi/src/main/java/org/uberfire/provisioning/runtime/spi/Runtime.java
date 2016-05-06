/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.uberfire.provisioning.runtime.spi.providers.Provider;

/**
 *
 * @author salaboy
 *
 * This class represent a Docker Image running or a WAR deployed into a server
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT)
public interface Runtime {

    public String getId();
    
    public void setId(String id);

    public void start();

    public void stop();

    public void restart();

    public RuntimeInfo getInfo();

    public RuntimeState getState();

    public Provider getProvider();

    public RuntimeConfiguration getConfig();
    
    public void setConfig(RuntimeConfiguration config);
    
    public void setProvider(Provider provider);
}
