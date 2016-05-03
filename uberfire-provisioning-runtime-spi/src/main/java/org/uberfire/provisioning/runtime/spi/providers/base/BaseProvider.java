/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers.base;

import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

/**
 *
 * @author salaboy
 */
public abstract class BaseProvider implements Provider {

    protected String name;
    protected ProviderConfiguration config;
    protected ProviderType providerType;


    public BaseProvider(String name, ProviderType providerType) {
        this.name = name;
        this.providerType = providerType;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ProviderConfiguration getConfig() {
        return config;
    }

    @Override
    public ProviderType getProviderType() {
        return providerType;
    }

}
