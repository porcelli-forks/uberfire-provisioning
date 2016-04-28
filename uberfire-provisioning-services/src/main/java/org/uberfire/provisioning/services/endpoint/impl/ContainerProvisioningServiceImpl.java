package org.uberfire.provisioning.services.endpoint.impl;

import com.spotify.docker.client.shaded.javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.uberfire.provisioning.cluster.ContainerRegistry;

import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.services.endpoint.impl.factories.ContainerProviderInstanceFactory;
import org.uberfire.provisioning.services.info.ContainerInstanceProviderInfo;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.base.BaseContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.providers.ContainerProvider;
import org.uberfire.provisioning.spi.providers.ContainerProviderInstance;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProviderConfiguration;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInfoImpl;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfoImpl;
import org.uberfire.provisioning.services.endpoint.api.ContainerProvisioningService;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class ContainerProvisioningServiceImpl implements ContainerProvisioningService {

    @Context
    private SecurityContext context;

    @Inject
    @Any
    private Instance<ContainerProvider> containerProvider;

    private Map<String, ContainerProviderInstance> containerInstanceProviders = new HashMap<String, ContainerProviderInstance>();

    @Inject
    private ContainerRegistry registry;

    private boolean initialized = false;

    public ContainerProvisioningServiceImpl() {

    }

    @PostConstruct
    public void cacheBeans() {
        if (!initialized) {
            registry.init();
            System.out.println(">>> After Init");
            for (ContainerProvider p : containerProvider) {
                System.out.println(">> New Container Instance Provider Found: " + p);
                registry.registerContainerProvider(p.getProviderName(), new ContainerProviderInfoImpl(p.getProviderName(), p.getVersion()));
            }
            initialized = true;
        }

    }

    @Override
    public List<ContainerProviderInfo> getAllContainerProviders() throws BusinessException {
        if (!initialized) {
            cacheBeans();
        }
        return registry.getContainerProviders();
    }

    @Override
    public List<ContainerInstanceProviderInfo> getAllContainerProvidersInstances() throws BusinessException {
        List<ContainerInstanceProviderInfo> cipInfos = new ArrayList<ContainerInstanceProviderInfo>();
        for (ContainerProviderInstanceInfo cip : registry.getContainerProviderInstances()) {
            cipInfos.add(new ContainerInstanceProviderInfo(cip.getName(), cip.getProviderName(), cip.getConfig().getProperties()));
        }
        return cipInfos;
    }

    @Override
    public void registerContainerProviderInstance(BaseContainerProviderConfiguration conf) throws BusinessException {
        String name = conf.getProperties().get("name");
        String provider = conf.getProperties().get("provider");

        ContainerProviderInstanceInfo newInstanceProviderInfo = new ContainerProviderInstanceInfoImpl(name, provider, conf);

        registry.registerContainerProviderInstance(name, newInstanceProviderInfo);

    }

    @Override
    public void unregisterContainerProviderInstance(String instanceName) throws BusinessException {
        registry.removeContainerProviderInstance(instanceName);
    }

    @Override
    public String newContainerInstance(BaseContainerInstanceConfiguration conf) throws BusinessException {

        String providerString = conf.getProperties().get("providerName");
        System.out.println(">>>> Provider: " + providerString);

        ContainerProviderInstanceInfo providerInfo = registry.getContainerProviderInstanceByName(providerString);

        if (providerInfo != null) {
            try {
                ContainerProviderInstance cpi = ContainerProviderInstanceFactory.newContainerProviderInstance(providerInfo, conf);
                containerInstanceProviders.put(cpi.getContainerInstanceInfo().getId(), cpi);
                registry.registerContainerInstanceByProvider(cpi.getProviderName(), cpi.getContainerInstanceInfo());
                return cpi.getContainerInstanceInfo().getId();
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(ContainerProvisioningServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "error";
    }

    @Override
    public List<ContainerInstanceInfo> getAllContainerInstances() throws BusinessException {
        return registry.getAllContainerInstances();
    }

    @Override
    public void removeContainerInstance(String id) throws BusinessException {

        containerInstanceProviders.remove(id);

        registry.removeContainerInstance(id);

    }

    @Override
    public void startContainerInstance(String id) throws BusinessException {
        ContainerProviderInstance cpi = containerInstanceProviders.get(id);
        if (cpi == null) {
            // The Container Instance might not be created in this node yet
            ContainerInstanceInfo containerInstanceById = registry.getContainerInstanceById(id);
            //try creating the container instance locally to execute the operation
            if (containerInstanceById == null) {
                throw new BusinessException("Container Instance cannot be found!");
            }
        }

        cpi.start();

    }

    @Override
    public void stopContainerInstance(String id) throws BusinessException {
        ContainerProviderInstance cpi = containerInstanceProviders.get(id);
        if (cpi == null) {
            // The Container Instance might not be created in this node yet
            ContainerInstanceInfo containerInstanceById = registry.getContainerInstanceById(id);
            //try creating the container instance locally to execute the operation
            if (containerInstanceById == null) {
                throw new BusinessException("Container Instance cannot be found!");
            }
        }

        cpi.stop();

    }

    @Override
    public void restartContainerInstance(String id) throws BusinessException {
        ContainerProviderInstance cpi = containerInstanceProviders.get(id);
        if (cpi == null) {
            // The Container Instance might not be created in this node yet
            ContainerInstanceInfo containerInstanceById = registry.getContainerInstanceById(id);
            //try creating the container instance locally to execute the operation
            if (containerInstanceById == null) {
                throw new BusinessException("Container Instance cannot be found!");
            }
        }

        cpi.restart();

    }

}
