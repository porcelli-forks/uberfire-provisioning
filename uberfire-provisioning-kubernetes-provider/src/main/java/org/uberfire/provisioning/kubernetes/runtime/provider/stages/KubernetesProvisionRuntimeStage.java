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

package org.uberfire.provisioning.kubernetes.runtime.provider.stages;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProvider;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesRuntimeConfBuilder;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;

public class KubernetesProvisionRuntimeStage implements Stage {

    @Override
    public String getName() {
        return "Provision new Runtime in Kubernetes";
    }

    @Override
    public void execute( PipelineContext context ) {
        String providerName = ( String ) context.getData().get( "providerName" );
        String namespace = ( String ) context.getData().get( "namespace" );
        String label = ( String ) context.getData().get( "label" );
        String serviceName = ( String ) context.getData().get( "serviceName" );
        String internalPort = ( String ) context.getData().get( "internalPort" );
        String image = ( String ) context.getData().get( "image" );

        RuntimeConfiguration kubernetesRuntimeConfig = KubernetesRuntimeConfBuilder.newConfig()
                .setNamespace( namespace )
                .setLabel( label )
                .setServiceName( serviceName )
                .setImage( image )
                .setInternalPort( internalPort )
                .get();

        RuntimeRegistry runtimeRegistry = ( RuntimeRegistry ) context.getServices().get( "runtimeRegistry" );
        KubernetesProvider kubernetesProvider = ( KubernetesProvider ) runtimeRegistry.getProvider( providerName );
        org.uberfire.provisioning.runtime.Runtime newKubernetesRuntime;
        try {
            newKubernetesRuntime = kubernetesProvider.create( kubernetesRuntimeConfig );
            runtimeRegistry.registerRuntime( newKubernetesRuntime );
            context.getOutput().put( "runtimeId", newKubernetesRuntime.getId() );
        } catch ( ProvisioningException ex ) {
            ex.printStackTrace();
            Logger.getLogger( KubernetesProvisionRuntimeStage.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Stage ) ) {
            return false;
        }

        final Stage that = ( Stage ) o;

        return getName() != null ? getName().equals( that.getName() ) : that.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

}
