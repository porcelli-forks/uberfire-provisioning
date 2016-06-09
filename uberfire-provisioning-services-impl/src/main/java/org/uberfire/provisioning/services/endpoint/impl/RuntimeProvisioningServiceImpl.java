/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.provisioning.services.endpoint.impl;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.services.endpoint.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.services.endpoint.impl.factories.ProviderFactory;

public class RuntimeProvisioningServiceImpl implements RuntimeProvisioningService {

    @Inject
    private BeanManager beanManager;

    @Inject
    private RuntimeRegistry registry;

    private boolean initialized = false;

    @PostConstruct
    public void cacheBeans() {
        if ( !initialized ) {
            final Set<Bean<?>> beans = beanManager.getBeans( ProviderType.class, new AnnotationLiteral<Any>() {
            } );
            for ( final Bean b : beans ) {
                try {
                    // I don't want to register the CDI proxy, I need a fresh instance :(
                    registry.registerProviderType( (ProviderType) b.getBeanClass().newInstance() );
                } catch ( InstantiationException | IllegalAccessException ex ) {
                    Logger.getLogger( RuntimeProvisioningServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
                }
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
    public void registerProvider( ProviderConfiguration conf ) throws BusinessException {
        Provider newProvider = ProviderFactory.newProvider( conf );
        registry.registerProvider( newProvider );
    }

    @Override
    public void unregisterProvider( String name ) throws BusinessException {
        registry.unregisterProvider( name );
    }

    @Override
    public String newRuntime( RuntimeConfiguration conf ) throws BusinessException {
        String providerName = conf.getProviderName();
        Provider provider = registry.getProvider( providerName );
        Runtime runtime;
        try {
            runtime = provider.create( conf );
            registry.registerRuntime( runtime );
            return runtime.getId();
        } catch ( Exception ex ) {
            Logger.getLogger( RuntimeProvisioningServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Runtime Creation for provider " + providerName + "Failed", ex );
        }

    }

    @Override
    public List<Runtime> getAllRuntimes() throws BusinessException {
        return registry.getAllRuntimes();
    }

    @Override
    public void unregisterRuntime( String id ) throws BusinessException {
        registry.unregisterRuntime( id );
    }

    @Override
    public void startRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        runtimeById.start();
    }

    @Override
    public void stopRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        runtimeById.stop();
    }

    @Override
    public void restartRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        runtimeById.restart();
    }

}
