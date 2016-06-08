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
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.runtime.providers.base.BaseProvider;

import static java.util.UUID.*;

/**
 * @author salaboy
 */
public class LocalProvider extends BaseProvider {

    public LocalProvider() {
    }

    public LocalProvider( ProviderConfiguration config ) {
        this( config, new LocalProviderType() );
    }

    public LocalProvider( ProviderConfiguration config,
                          ProviderType type ) {
        super( config.getName(), type );
        this.config = config;

    }

    @Override
    public Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {
        String shortId = randomUUID().toString().substring( 0, 12 );
        return new LocalRuntime( shortId, runtimeConfig, this );

    }

    @Override
    public void destroy( String runtimeId ) throws ProvisioningException {

    }

}
