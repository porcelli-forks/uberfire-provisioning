/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers.base;

import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

/**
 *
 * @author salaboy
 */
public abstract class BaseProviderType implements ProviderType {

    private final String providerName;
    private final String version;
    private final Class provider;

    public BaseProviderType(String providerName, String version, Class provider) {
        this.providerName = providerName;
        this.version = version;
        this.provider = provider;
    }

    @Override
    public String getProviderTypeName() {
        return providerName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public Class getProvider() {
        return provider;
    }
    
    

}
