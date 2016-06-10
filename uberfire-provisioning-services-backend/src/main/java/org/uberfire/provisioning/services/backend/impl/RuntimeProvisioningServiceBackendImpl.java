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

package org.uberfire.provisioning.services.backend.impl;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jboss.errai.bus.server.annotations.Service;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.services.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.api.backend.RuntimeProvisioningServiceBackend;
import org.uberfire.provisioning.services.exceptions.BusinessException;

@Service
@ApplicationScoped
public class RuntimeProvisioningServiceBackendImpl implements RuntimeProvisioningServiceBackend {

    private RuntimeProvisioningService runtimeProvisioningService;

    public RuntimeProvisioningServiceBackendImpl() {
    }

    @Inject
    public RuntimeProvisioningServiceBackendImpl( final RuntimeProvisioningService runtimeProvisioningService ) {
        this.runtimeProvisioningService = runtimeProvisioningService;
    }

    @Override
    public List<ProviderType> getAllProviderTypes() throws BusinessException {
        return runtimeProvisioningService.getAllProviderTypes();
    }

    @Override
    public List<Provider> getAllProviders() throws BusinessException {
        return runtimeProvisioningService.getAllProviders();
    }

    @Override
    public void registerProvider( @NotNull ProviderConfiguration conf ) throws BusinessException {
        runtimeProvisioningService.registerProvider( conf );
    }

    @Override
    public void unregisterProvider( final String name ) throws BusinessException {
        runtimeProvisioningService.unregisterProvider( name );
    }

    @Override
    public String newRuntime( final RuntimeConfiguration conf ) throws BusinessException {
        return runtimeProvisioningService.newRuntime( conf );
    }

    @Override
    public List<Runtime> getAllRuntimes() throws BusinessException {
        return runtimeProvisioningService.getAllRuntimes();
    }

    @Override
    public void unregisterRuntime( final String id ) throws BusinessException {
        runtimeProvisioningService.unregisterRuntime( id );
    }

    @Override
    public void startRuntime( final String runtimeId ) throws BusinessException {
        runtimeProvisioningService.startRuntime( runtimeId );
    }

    @Override
    public void stopRuntime( final String runtimeId ) throws BusinessException {
        runtimeProvisioningService.stopRuntime( runtimeId );
    }

    @Override
    public void restartRuntime( final String runtimeId ) throws BusinessException {
        runtimeProvisioningService.restartRuntime( runtimeId );
    }

}
