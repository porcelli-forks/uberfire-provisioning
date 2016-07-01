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

package org.uberfire.provisioning.wildfly.runtime.provider.stages;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeConfBuilder;

public class WildflyProvisionRuntimeStage implements Stage {

    @Override
    public String getName() {
        return "Wildfly Provisio Runtime Stage";
    }

    @Override
    public void execute( PipelineContext context ) {
        String providerName = ( String ) context.getData().get( "providerName" );
        String warPath = ( String ) context.getData().get( "warPath" );
        String appContext = ( String ) context.getData().get( "context" );

        RuntimeConfiguration wildflyRuntimeConfig = WildflyRuntimeConfBuilder.newConfig()
                .setProviderName( providerName )
                .setContext( appContext )
                .setWarPath( warPath )
                .get();

        RuntimeRegistry runtimeRegistry = ( RuntimeRegistry ) context.getServices().get( "runtimeRegistry" );
        Provider provider = runtimeRegistry.getProvider( providerName );

        try {
            Runtime newWildflyRuntime = provider.create( wildflyRuntimeConfig );
            runtimeRegistry.registerRuntime( newWildflyRuntime );
            context.getOutput().put( "runtimeId", newWildflyRuntime.getId() );
        } catch ( ProvisioningException ex ) {
            Logger.getLogger( WildflyProvisionRuntimeStage.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

}
