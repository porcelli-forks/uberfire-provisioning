/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.local.runtime.provider;

import java.util.UUID;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProvider;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.exception.ProvisioningException;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

/**
 *
 * @author salaboy
 */
public class LocalProvider extends BaseProvider {



    public LocalProvider(ProviderConfiguration config) {
        this(config, new LocalProviderType());
    }

    public LocalProvider(ProviderConfiguration config, ProviderType type) {
        super(config.getName(), type);
        this.config = config;
       

    }

    @Override
    public Runtime create(RuntimeConfiguration runtimeConfig) throws ProvisioningException {

        String shortId = UUID.randomUUID().toString();
        return new LocalRuntime(shortId, runtimeConfig, this);

    }

    

    @Override
    public void destroy(String runtimeId) throws ProvisioningException {
        

    }

}
