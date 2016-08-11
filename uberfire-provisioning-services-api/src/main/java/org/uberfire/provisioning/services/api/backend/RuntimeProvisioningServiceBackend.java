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

package org.uberfire.provisioning.services.api.backend;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.uberfire.provisioning.config.RuntimeConfig;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.services.exceptions.BusinessException;

@Remote
public interface RuntimeProvisioningServiceBackend {

    List<ProviderType> getAllProviderTypes() throws BusinessException;

    List<Provider> getAllProviders() throws BusinessException;

    void registerProvider( final RuntimeConfig conf ) throws BusinessException;

    void unregisterProvider( final String name ) throws BusinessException;

    String newRuntime( final RuntimeConfig conf ) throws BusinessException;

    List<Runtime> getAllRuntimes() throws BusinessException;

    void destroyRuntime( final String runtimeId ) throws BusinessException;

    void startRuntime( final String runtimeId ) throws BusinessException;

    void stopRuntime( final String runtimeId ) throws BusinessException;

    void restartRuntime( final String runtimeId ) throws BusinessException;

}
