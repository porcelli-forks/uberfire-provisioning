/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.base;

import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.Runtime;

/**
 *
 * @author salaboy
 */
public abstract class BaseRuntime implements Runtime {

    private String id;
    protected RuntimeConfiguration config;
    protected Provider provider;

    public BaseRuntime(String id, RuntimeConfiguration config, Provider provider) {
        this.id = id;
        this.config = config;
        this.provider = provider;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Provider getProvider() {
        return provider;
    }

    @Override
    public RuntimeConfiguration getConfig() {
        return config;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setConfig(RuntimeConfiguration config) {
        this.config = config;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

}
