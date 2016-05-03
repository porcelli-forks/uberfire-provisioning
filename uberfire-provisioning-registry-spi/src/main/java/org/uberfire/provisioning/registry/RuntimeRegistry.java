/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry;

import java.util.List;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.Runtime;

/**
 *
 * @author salaboy
 */
public interface RuntimeRegistry {

    public void registerProviderType(ProviderType pt);

    public List<ProviderType> getAllProviderTypes();

    public ProviderType getProviderTypeByName(String provider);
    
    public void unregisterProviderType(ProviderType providerType);
    
    public void registerProvider(Provider provider);

    public Provider getProvider(String providerName);
    
    public List<Provider> getAllProviders();
    
    public List<Provider> getProvidersByType(ProviderType type);
    
    public void unregisterProvider(Provider provider);
    
    public void unregisterProvider(String providerName);
    
    public void registerRuntime(Runtime runtime);
    
    public List<Runtime> getAllRuntimes();
    
    public List<Runtime> getRuntimesByProvider(ProviderType provider);
    
    public Runtime getRuntimeById(String id);
    
    public void unregisterRuntime(Runtime runtime);
    
    public void unregisterRuntime(String runtimeId);


}
