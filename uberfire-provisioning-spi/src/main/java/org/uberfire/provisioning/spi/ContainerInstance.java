/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.spi;

import org.uberfire.provisioning.spi.providers.ContainerProviderInstance;

/**
 *
 * @author salaboy
 *
 * This class represent a Docker Image running or a WAR deployed into a server
 *
 */
public interface ContainerInstance {
    
    public void setProvider(ContainerProviderInstance provider);

    public void start();

    public void stop();
    
    public void restart();

    public ContainerInstanceInfo getInfo();
    
    public ContainerInstanceState getState();
}
