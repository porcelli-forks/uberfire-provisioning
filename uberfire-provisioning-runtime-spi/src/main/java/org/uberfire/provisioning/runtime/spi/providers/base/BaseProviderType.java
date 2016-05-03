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

    public BaseProviderType(String providerName, String version) {
        this.providerName = providerName;
        this.version = version;
    }

    @Override
    public String getProviderTypeName() {
        return providerName;
    }

    @Override
    public String getVersion() {
        return version;
    }
    
    

}
