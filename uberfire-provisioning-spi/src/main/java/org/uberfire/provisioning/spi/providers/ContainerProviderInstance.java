/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.spi.providers;

import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;

/**
 *
 * @author salaboy
 */
public interface ContainerProviderInstance {

    public String getName();

    public ContainerInstanceConfiguration getConfig();
    
    public ContainerInstanceInfo getContainerInstanceInfo();
    
    public ContainerProviderInstanceInfo getContainerProviderInstanceInfo();

    public String getProviderName();

    public void setProviderName(String providerName);

    public ContainerInstanceInfo create() throws Exception;

    public void start();

    public void stop();

    public void restart();
}
