/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.cluster;

import java.util.List;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;

/**
 *
 * @author salaboy
 */
public interface ContainerRegistry {

    public void init();

    public void registerContainerProvider(String providerName, ContainerProviderInfo p);

    public List<ContainerProviderInfo> getContainerProviders();

    public ContainerProviderInfo getContainerProviderByName(String provider);

    public List<ContainerProviderInstanceInfo> getContainerProviderInstances();

    public void registerContainerProviderInstance(String name, ContainerProviderInstanceInfo newInstanceProvider);

//    public void registerContainerProviderInstanceByProvider(String provider, ContainerProviderInstanceInfo newInstanceProvider);
    public ContainerProviderInstanceInfo getContainerProviderInstanceByName(String instanceName);

    public void removeContainerProviderInstance(String instanceName);
//
//    public ContainerProviderInfo getContainerProvider(String providerString);
    public void registerContainerInstanceByProvider(String providerName, ContainerInstanceInfo ci);

//    public List<ContainerInstanceInfo> getContainerInstanceByProvider(String providerString);
//
//    public List<String> getContainerInstancesProviderNames();
//
//    public List<ContainerProviderInstanceInfo> getContainerProviderInstanceByProvider(String providerString);
//
//    public void registerAllContainerInstanceByProvider(String provider, List<ContainerInstanceInfo> allInstances);
//
    public List<ContainerInstanceInfo> getAllContainerInstances();

    public ContainerInstanceInfo getContainerInstanceById(String id);

    public void removeContainerInstance(String id);

}
