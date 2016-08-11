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

package org.uberfire.provisioning.local.runtime.provider;

import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderService;

import static java.util.UUID.*;

public class LocalProviderService implements ProviderService {

    private LocalProvider provider;

    public LocalProviderService( LocalProvider provider ) {
        this.provider = provider;
    }

    @Override
    public org.uberfire.provisioning.runtime.Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {
        String shortId = randomUUID().toString().substring( 0, 12 );
        return new LocalRuntime( shortId, runtimeConfig, provider );

    }

    @Override
    public void destroy( String runtimeId ) throws ProvisioningException {

    }
}
