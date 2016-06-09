/*
 * Copyright 2016 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.provisioning.remote.client;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.services.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.exceptions.BusinessException;

@ApplicationScoped
public class RestClientRuntimeProvisioningService implements RuntimeProvisioningService {

    private RuntimeProvisioningService remoteService;

    @PostConstruct
    public void init() {
        final Client client = ClientBuilder.newBuilder().build();
        final WebTarget target = client.target( "http://localhost:8082/api/" );
        final ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
        remoteService = rtarget.proxy( RuntimeProvisioningService.class );
    }

    @Override
    public List<ProviderType> getAllProviderTypes() throws BusinessException {
        return remoteService.getAllProviderTypes();
    }

    @Override
    public List<Provider> getAllProviders() throws BusinessException {
        return remoteService.getAllProviders();
    }

    @Override
    public void registerProvider( ProviderConfiguration conf ) throws BusinessException {
        remoteService.registerProvider( conf );
    }

    @Override
    public void unregisterProvider( String name ) throws BusinessException {
        remoteService.unregisterProvider( name );
    }

    @Override
    public String newRuntime( RuntimeConfiguration conf ) throws BusinessException {
        String newRuntime = null;
        try {
            newRuntime = remoteService.newRuntime( conf );
        } catch ( Exception ex ) {
            throw new BusinessException( "Error Creating Remote Runtime", ex );
        }
        return newRuntime;
    }

    @Override
    public List<Runtime> getAllRuntimes() throws BusinessException {
        return remoteService.getAllRuntimes();
    }

    @Override
    public void unregisterRuntime( String id ) throws BusinessException {
        remoteService.unregisterRuntime( id );
    }

    @Override
    public void startRuntime( String runtimeId ) throws BusinessException {
        remoteService.startRuntime( runtimeId );
    }

    @Override
    public void stopRuntime( String runtimeId ) throws BusinessException {
        remoteService.stopRuntime( runtimeId );
    }

    @Override
    public void restartRuntime( String runtimeId ) throws BusinessException {
        remoteService.restartRuntime( runtimeId );
    }

}
