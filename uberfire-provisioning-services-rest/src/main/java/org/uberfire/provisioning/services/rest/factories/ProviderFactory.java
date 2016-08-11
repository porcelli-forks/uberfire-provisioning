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

package org.uberfire.provisioning.services.rest.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.uberfire.provisioning.config.ProviderConfig;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderBuilder;

public class ProviderFactory {

    private final Collection<ProviderBuilder> builders = new ArrayList<>();

    @Inject
    public ProviderFactory( final Instance<ProviderBuilder> builders ) {
        builders.forEach( this.builders::add );
    }

    public Optional<Provider> newProvider( ProviderConfig config ) {
        final Optional<ProviderBuilder> providerBuilder = builders.stream()
                .filter( p -> p.supports( config ) )
                .findFirst();
        if ( providerBuilder.isPresent() ) {
            return (Optional<Provider>) providerBuilder.get().apply( config );
        }
        return Optional.empty();
    }
}
