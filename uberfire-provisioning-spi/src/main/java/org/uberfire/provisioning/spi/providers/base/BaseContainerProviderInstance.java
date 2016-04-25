/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.spi.providers.base;

import org.uberfire.provisioning.spi.providers.ContainerProviderInstance;
import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;

/**
 *
 * @author salaboy
 */
public abstract class BaseContainerProviderInstance implements ContainerProviderInstance {

    protected String name;
    protected String providerName;
    protected ContainerInstanceConfiguration config;
    protected ContainerInstanceInfo containerInstanceInfo;
    protected ContainerProviderInstanceInfo containerProviderInstanceInfo;
    
    public BaseContainerProviderInstance() {
    }

    public BaseContainerProviderInstance(String name, String providerName) {
        this.name = name;
        this.providerName = providerName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ContainerInstanceConfiguration getConfig() {
        return config;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public ContainerInstanceInfo getContainerInstanceInfo() {
        return containerInstanceInfo;
    }

    @Override
    public ContainerProviderInstanceInfo getContainerProviderInstanceInfo() {
        return containerProviderInstanceInfo;
    }
    
    
    
    
    

}
