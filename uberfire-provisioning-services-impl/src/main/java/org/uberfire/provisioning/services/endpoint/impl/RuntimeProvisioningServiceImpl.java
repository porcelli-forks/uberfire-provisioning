package org.uberfire.provisioning.services.endpoint.impl;



import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.services.endpoint.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.endpoint.impl.factories.ProviderFactory;
import org.uberfire.provisioning.runtime.spi.Runtime;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class RuntimeProvisioningServiceImpl implements RuntimeProvisioningService {

    @Context
    private SecurityContext context;

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    @Inject
    private RuntimeRegistry registry;

    private boolean initialized = false;

    public RuntimeProvisioningServiceImpl() {

    }

    @PostConstruct
    public void cacheBeans() {
        if (!initialized) {
            for (ProviderType pt : providerTypes) {
                System.out.println(">> New Provider Type Found: " + pt);
                registry.registerProviderType(pt);
            }
            initialized = true;
        }

    }

    @Override
    public List<ProviderType> getAllProviderTypes() throws BusinessException {
        return registry.getAllProviderTypes();
    }

    @Override
    public List<Provider> getAllProviders() throws BusinessException {
        return registry.getAllProviders();
    }

    @Override
    public void registerProvider(ProviderConfiguration conf) throws BusinessException {
        Provider newProvider = ProviderFactory.newProvider(conf);
        registry.registerProvider(newProvider);
    }

    @Override
    public void unregisterProvider(String name) throws BusinessException {
        registry.unregisterProvider(name);
    }

    @Override
    public String newRuntime(RuntimeConfiguration conf) throws BusinessException {
        String providerName = conf.getProviderName();
        Provider provider = registry.getProvider(providerName); 
        Runtime runtime;
        try {
            runtime = provider.create(conf);
            registry.registerRuntime(runtime);
            return runtime.getId();
        } catch (Exception ex) {
            Logger.getLogger(RuntimeProvisioningServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public List<Runtime> getAllRuntimes() throws BusinessException {
        return registry.getAllRuntimes();
    }

    @Override
    public void unregisterRuntime(String id) throws BusinessException {
        registry.unregisterRuntime(id);
    }

    @Override
    public void startRuntime(String runtimeId) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById(runtimeId);
        runtimeById.start();
    }

    @Override
    public void stopRuntime(String runtimeId) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById(runtimeId);
        runtimeById.stop();
    }

    @Override
    public void restartRuntime(String runtimeId) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById(runtimeId);
        runtimeById.restart();
    }
    
    

}
