/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.cluster.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.uberfire.provisioning.cluster.ContainerRegistry;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInfo;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;

/**
 *
 * @author salaboy This is a not thread-safe implementation for local testing. A
 * more robust implementation should be provided
 */
public class InMemoryContainerRegistry implements ContainerRegistry {

    private Map<String, ContainerProviderInfo> containerProviders;
    private Map<String, ContainerProviderInstanceInfo> containerProviderInstances;
    private Map<String, List<ContainerProviderInstanceInfo>> containerProviderInstancesByProvider;
    private Map<String, List<ContainerInstanceInfo>> containerInstancesByProvider;

    @Override
    public void init() {
        containerProviders = new HashMap<>();
        containerProviderInstances = new HashMap<>();
        containerInstancesByProvider = new HashMap<>();
    }

    @Override
    public void registerContainerProvider(String providerName, ContainerProviderInfo p) {
        containerProviders.put(providerName, p);
    }

    @Override
    public List<ContainerProviderInfo> getContainerProviders() {
        return new ArrayList<>(containerProviders.values());
    }

    @Override
    public ContainerProviderInfo getContainerProviderByName(String provider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ContainerProviderInstanceInfo> getContainerProviderInstances() {
        return new ArrayList<>(containerProviderInstances.values());
    }

    @Override
    public void registerContainerProviderInstance(String name, ContainerProviderInstanceInfo newInstanceProvider) {
        containerProviderInstances.put(name, newInstanceProvider);
    }

//    @Override
//    public void registerContainerProviderInstanceByProvider(String provider, ContainerProviderInstanceInfo newInstanceProvider) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public ContainerProviderInstanceInfo getContainerProviderInstanceByName(String instanceName) {
        return containerProviderInstances.get(instanceName);
    }

    @Override
    public void removeContainerProviderInstance(String instanceName) {
        containerProviderInstances.remove(instanceName);
    }
//
//    @Override
//    public ContainerProviderInfo getContainerProvider(String providerString) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public void registerContainerInstanceByProvider(String providerName, ContainerInstanceInfo ci) {
        if (containerInstancesByProvider.get(providerName) == null) {
            containerInstancesByProvider.put(providerName, new ArrayList<>());
        }
        containerInstancesByProvider.get(providerName).add(ci);
    }

//    @Override
//    public List<ContainerInstanceInfo> getContainerInstanceByProvider(String providerString) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<String> getContainerInstancesProviderNames() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<ContainerProviderInstanceInfo> getContainerProviderInstanceByProvider(String providerString) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void registerAllContainerInstanceByProvider(String provider, List<ContainerInstanceInfo> allInstances) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public List<ContainerInstanceInfo> getAllContainerInstances() {
        List<ContainerInstanceInfo> containerInstances = new ArrayList<>();
        for (List<ContainerInstanceInfo> byContainer : containerInstancesByProvider.values()) {
            containerInstances.addAll(byContainer);
        }
        return containerInstances;
    }

    @Override
    public ContainerInstanceInfo getContainerInstanceById(String id) {
        for (String provider : containerInstancesByProvider.keySet()) {
            for (ContainerInstanceInfo cii : containerInstancesByProvider.get(provider)) {
                if (cii.getId().equals(id)) {
                    return cii;
                }
            }
        }
        return null;
    }

    @Override
    public void removeContainerInstance(String id) {
        for (String provider : containerInstancesByProvider.keySet()) {
            Iterator<ContainerInstanceInfo> iterator = containerInstancesByProvider.get(provider).iterator();
            while (iterator.hasNext()) {
                ContainerInstanceInfo cii = iterator.next();
                if (cii.getId().equals(id)) {
                    iterator.remove();
                }
            }
        }
    }

}
