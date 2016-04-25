/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.spi.providers.info;

import org.uberfire.provisioning.spi.providers.ContainerProviderConfiguration;

/**
 *
 * @author salaboy
 */
public class ContainerProviderInstanceInfoImpl implements ContainerProviderInstanceInfo {

    private String name;
    private String providerName;
    private ContainerProviderConfiguration config;

    public ContainerProviderInstanceInfoImpl() {
    }

    public ContainerProviderInstanceInfoImpl(String name, String providerName, ContainerProviderConfiguration config) {
        this.name = name;
        this.providerName = providerName;
        this.config = config;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public ContainerProviderConfiguration getConfig() {
        return config;
    }

}
