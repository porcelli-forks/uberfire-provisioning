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

package org.uberfire.provisioning.services.rest;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.uberfire.provisioning.exceptions.ProvisioningException;

import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderService;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.services.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.exceptions.BusinessException;
import org.uberfire.provisioning.services.rest.factories.ProviderFactory;

@ApplicationScoped
public class RestRuntimeProvisioningServiceImpl implements RuntimeProvisioningService {

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
                    registry.registerProviderType( ( ProviderType ) b.getBeanClass().newInstance() );
                } catch ( InstantiationException | IllegalAccessException ex ) {
                    Logger.getLogger( RestRuntimeProvisioningServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
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
    public void registerProvider( @NotNull ProviderConfiguration conf ) throws BusinessException {
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

        ProviderService providerService = createProviderService( provider );
        Runtime runtime;
        try {
            runtime = providerService.create( conf );
            registry.registerRuntime( runtime );
            return runtime.getId();
        } catch ( Exception ex ) {
            Logger.getLogger( RestRuntimeProvisioningServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Runtime Creation for provider " + providerName + "Failed", ex );
        }

    }

    @Override
    public void destroyRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        Provider provider = runtimeById.getProvider();
        ProviderService providerService = createProviderService( provider );
        try {
            providerService.destroy( runtimeId );
            registry.unregisterRuntime( runtimeById );
        } catch ( ProvisioningException ex ) {
            Logger.getLogger( RestRuntimeProvisioningServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
            throw new BusinessException( "Runtime Destruction failed for runtimeId: " + runtimeId, ex );
        }
    }

    @Override
    public List<Runtime> getAllRuntimes() throws BusinessException {
        return registry.getAllRuntimes();
    }

    @Override
    public void startRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        RuntimeService runtimeService = createRuntimeService( runtimeById );
        runtimeService.start();
    }

    @Override
    public void stopRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        RuntimeService runtimeService = createRuntimeService( runtimeById );
        runtimeService.stop();
    }

    @Override
    public void restartRuntime( String runtimeId ) throws BusinessException {
        Runtime runtimeById = registry.getRuntimeById( runtimeId );
        RuntimeService runtimeService = createRuntimeService( runtimeById );
        runtimeService.restart();
    }

    private ProviderService createProviderService( Provider provider ) {
        Class providerService = provider.getProviderType().getProviderService();
        try {
            Constructor c = providerService.getConstructor( provider.getClass() );
            ProviderService service = ( ProviderService ) c.newInstance( provider );
            return service;
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return null;
    }

    private RuntimeService createRuntimeService( Runtime runtime ) {
        Class runtimeService = runtime.getProvider().getProviderType().getRuntimeService();
        try {
            Constructor c = runtimeService.getConstructor( runtime.getClass() );
            RuntimeService service = ( RuntimeService ) c.newInstance( runtime );
            return service;
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return null;
    }

}
