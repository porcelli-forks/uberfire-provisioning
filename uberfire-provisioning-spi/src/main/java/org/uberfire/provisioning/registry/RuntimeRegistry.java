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

package org.uberfire.provisioning.registry;

import java.util.List;

import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderType;

/**
 * @author salaboy
 */
public interface RuntimeRegistry {

    void registerProviderType( ProviderType pt );

    List<ProviderType> getAllProviderTypes();

    ProviderType getProviderTypeByName( String provider );

    void unregisterProviderType( ProviderType providerType );

    void registerProvider( Provider provider );

    Provider getProvider( String providerName );

    List<Provider> getAllProviders();

    List<Provider> getProvidersByType( ProviderType type );

    void unregisterProvider( Provider provider );

    void unregisterProvider( String providerName );

    void registerRuntime( Runtime runtime );

    List<Runtime> getAllRuntimes();

    List<Runtime> getRuntimesByProvider( ProviderType provider );

    Runtime getRuntimeById( String id );

    void unregisterRuntime( Runtime runtime );


}
