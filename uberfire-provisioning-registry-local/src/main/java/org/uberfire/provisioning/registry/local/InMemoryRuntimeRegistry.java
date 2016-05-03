/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.Runtime;

/**
 *
 * @author salaboy This is a not thread-safe implementation for local testing. A
 * more robust implementation should be provided for real usecases
 */
public class InMemoryRuntimeRegistry implements RuntimeRegistry {

    private final Map<String, ProviderType> providerTypes;
    private final Map<String, Provider> providers;
    private final Map<ProviderType, List<Provider>> providersByType;
    private final Map<ProviderType, List<Runtime>> runtimesByProviderType;

    public InMemoryRuntimeRegistry() {
        providerTypes = new HashMap<>();
        providers = new HashMap<>();
        providersByType = new HashMap<>();
        runtimesByProviderType = new HashMap<>();
    }

    @Override
    public void registerProviderType(ProviderType pt) {
        providerTypes.put(pt.getProviderTypeName(), pt);
    }

    @Override
    public List<ProviderType> getAllProviderTypes() {
        return new ArrayList<>(providerTypes.values());
    }

    @Override
    public ProviderType getProviderTypeByName(String provider) {
        return providerTypes.get(provider);
    }

    @Override
    public void unregisterProviderType(ProviderType providerType) {
        providerTypes.remove(providerType.getProviderTypeName());
    }

    @Override
    public void registerProvider(Provider provider) {
        providers.put(provider.getName(), provider);
        if (providersByType.get(provider.getProviderType()) == null) {
            providersByType.put(provider.getProviderType(), new ArrayList<>());
        }
        providersByType.get(provider.getProviderType()).add(provider);
    }

    @Override
    public List<Provider> getAllProviders() {
        return new ArrayList<>(providers.values());
    }

    @Override
    public List<Provider> getProvidersByType(ProviderType type) {
        return providersByType.get(type);
    }

    @Override
    public Provider getProvider(String providerName) {
        return providers.get(providerName);
    }

    @Override
    public void unregisterProvider(Provider provider) {
        providersByType.get(provider.getProviderType()).remove(provider);
        providers.remove(provider.getName());
    }

    @Override
    public void unregisterProvider(String providerName) {
        for (Provider p : providers.values()) {
            if (p.getName().equals(providerName)) {
                unregisterProvider(p);
            }
        }
    }

    @Override
    public void registerRuntime(Runtime runtime) {
        if (runtimesByProviderType.get(runtime.getProvider().getProviderType()) == null) {
            runtimesByProviderType.put(runtime.getProvider().getProviderType(), new ArrayList<>());
        }
        runtimesByProviderType.get(runtime.getProvider().getProviderType()).add(runtime);
    }

    @Override
    public List<Runtime> getAllRuntimes() {
        List<Runtime> runtimes = new ArrayList<>();
        for (List<Runtime> rs : runtimesByProviderType.values()) {
            runtimes.addAll(rs);
        }
        return runtimes;
    }

    @Override
    public List<Runtime> getRuntimesByProvider(ProviderType providerType) {
        return new ArrayList<>(runtimesByProviderType.get(providerType));
    }

    @Override
    public Runtime getRuntimeById(String id) {
        for (ProviderType pt : runtimesByProviderType.keySet()) {
            for (Runtime r : runtimesByProviderType.get(pt)) {
                if (r.getId().equals(id)) {
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public void unregisterRuntime(Runtime runtime) {
        runtimesByProviderType.get(runtime.getProvider().getProviderType()).remove(runtime);
    }

    @Override
    public void unregisterRuntime(String runtimeId) {
        for (ProviderType pt : runtimesByProviderType.keySet()) {
            for (Runtime r : runtimesByProviderType.get(pt)) {
                if (r.getId().equals(runtimeId)) {
                    unregisterRuntime(r);
                }
            }
        }
    }

}
