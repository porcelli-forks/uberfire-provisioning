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
        String providerName = conf.getProperties().get("providerName");
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

//    @Override
//    public List<ContainerProviderInfo> getAllContainerProviders() throws BusinessException {
//        if (!initialized) {
//            cacheBeans();
//        }
//        return registry.getContainerProviders();
//    }
//
//    @Override
//    public List<ContainerInstanceProviderInfo> getAllContainerProvidersInstances() throws BusinessException {
//        List<ContainerInstanceProviderInfo> cipInfos = new ArrayList<ContainerInstanceProviderInfo>();
//        for (ContainerProviderInstanceInfo cip : registry.getContainerProviderInstances()) {
//            cipInfos.add(new ContainerInstanceProviderInfo(cip.getName(), cip.getProviderName(), cip.getConfig().getProperties()));
//        }
//        return cipInfos;
//    }
//
//    @Override
//    public void registerContainerProviderInstance(ContainerProviderConfiguration conf) throws BusinessException {
//        String name = conf.getProperties().get("name");
//        String provider = conf.getProperties().get("provider");
//
//        ContainerProviderInstanceInfo newInstanceProviderInfo = new ContainerProviderInstanceInfoImpl(name, provider, conf);
//
//        registry.registerContainerProviderInstance(name, newInstanceProviderInfo);
//
//    }
//
//    @Override
//    public void unregisterContainerProviderInstance(String instanceName) throws BusinessException {
//        registry.removeContainerProviderInstance(instanceName);
//    }
//
//    @Override
//    public String newContainerInstance(RuntimeConfiguration conf) throws BusinessException {
//
//        String providerString = conf.getProperties().get("providerName");
//        System.out.println(">>>> Provider: " + providerString);
//
//        ContainerProviderInstanceInfo providerInfo = registry.getContainerProviderInstanceByName(providerString);
//
//        if (providerInfo != null) {
//            try {
//                ContainerProviderInstance cpi = ContainerProviderInstanceFactory.newContainerProviderInstance(providerInfo, conf);
//                containerInstanceProviders.put(cpi.getContainerInstanceInfo().getId(), cpi);
//                registry.registerContainerInstanceByProvider(cpi.getProviderName(), cpi.getContainerInstanceInfo());
//                return cpi.getContainerInstanceInfo().getId();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Logger.getLogger(RuntimeProvisioningServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return "error";
//    }
//
//    @Override
//    public List<RuntimeInfo> getAllContainerInstances() throws BusinessException {
//        return registry.getAllContainerInstances();
//    }
//
//    @Override
//    public void removeContainerInstance(String id) throws BusinessException {
//
//        containerInstanceProviders.remove(id);
//
//        registry.removeContainerInstance(id);
//
//    }
//
//    @Override
//    public void startContainerInstance(String id) throws BusinessException {
//        ContainerProviderInstance cpi = containerInstanceProviders.get(id);
//        if (cpi == null) {
//            // The Container Instance might not be created in this node yet
//            RuntimeInfo containerInstanceById = registry.getContainerInstanceById(id);
//            //try creating the container instance locally to execute the operation
//            if (containerInstanceById == null) {
//                throw new BusinessException("Container Instance cannot be found!");
//            }
//        }
//
//        cpi.start();
//
//    }
//
//    @Override
//    public void stopContainerInstance(String id) throws BusinessException {
//        ContainerProviderInstance cpi = containerInstanceProviders.get(id);
//        if (cpi == null) {
//            // The Container Instance might not be created in this node yet
//            RuntimeInfo containerInstanceById = registry.getContainerInstanceById(id);
//            //try creating the container instance locally to execute the operation
//            if (containerInstanceById == null) {
//                throw new BusinessException("Container Instance cannot be found!");
//            }
//        }
//
//        cpi.stop();
//
//    }
//
//    @Override
//    public void restartContainerInstance(String id) throws BusinessException {
//        ContainerProviderInstance cpi = containerInstanceProviders.get(id);
//        if (cpi == null) {
//            // The Container Instance might not be created in this node yet
//            RuntimeInfo containerInstanceById = registry.getContainerInstanceById(id);
//            //try creating the container instance locally to execute the operation
//            if (containerInstanceById == null) {
//                throw new BusinessException("Container Instance cannot be found!");
//            }
//        }
//
//        cpi.restart();
//
//    }
}
