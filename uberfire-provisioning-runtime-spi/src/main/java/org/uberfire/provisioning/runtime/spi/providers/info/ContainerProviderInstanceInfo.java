/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers.info;

import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;

/**
 *
 * @author salaboy
 */
public interface ContainerProviderInstanceInfo {
    public String getName();
    public String getProviderName();
    public ProviderConfiguration getConfig();
    
}
