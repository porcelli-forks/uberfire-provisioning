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

package org.uberfire.provisioning.openshift.runtime.provider.stages;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProvider;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderService;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntimeConfBuilder;
import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

public class OpenshiftProvisionRuntimeStage extends BaseStage {

    private String providerName;
    private String namespace;
    private String label;
    private String serviceName;
    private String internalPort;
    private String image;

    // Out Holders
    private String runtimeIdHolder;

    public OpenshiftProvisionRuntimeStage() {
        addRequiredService( RuntimeRegistry.class );
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName( String providerName ) {
        this.providerName = providerName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace( String namespace ) {
        this.namespace = namespace;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel( String label ) {
        this.label = label;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName( String serviceName ) {
        this.serviceName = serviceName;
    }

    public String getInternalPort() {
        return internalPort;
    }

    public void setInternalPort( String internalPort ) {
        this.internalPort = internalPort;
    }

    public String getImage() {
        return image;
    }

    public void setImage( String image ) {
        this.image = image;
    }

    public String getRuntimeIdHolder() {
        return runtimeIdHolder;
    }

    public void setRuntimeIdHolder( String runtimeIdHolder ) {
        this.runtimeIdHolder = runtimeIdHolder;
    }

    @Override
    public void execute( PipelineInstance pipe, PipelineDataContext pipeData ) {

        RuntimeConfiguration openshiftRuntimeConfig = OpenshiftRuntimeConfBuilder.newConfig()
                .setNamespace( getNamespace() )
                .setLabel( getLabel() )
                .setServiceName( getServiceName() )
                .setImage( getImage() )
                .setInternalPort( getInternalPort() )
                .get();
        RuntimeRegistry runtimeRegistry = pipe.getService( RuntimeRegistry.class );
        OpenshiftProvider openshiftProvider = ( OpenshiftProvider ) runtimeRegistry.getProvider( getProviderName() );
        OpenshiftProviderService providerService = new OpenshiftProviderService( openshiftProvider );

        org.uberfire.provisioning.runtime.Runtime newOpenshiftRuntime;
        try {

            newOpenshiftRuntime = providerService.create( openshiftRuntimeConfig );
            runtimeRegistry.registerRuntime( newOpenshiftRuntime );
            pipeData.setData( "${" + getRuntimeIdHolder() + "}", newOpenshiftRuntime.getId() );
        } catch ( ProvisioningException ex ) {
            ex.printStackTrace();
            Logger.getLogger(OpenshiftProvisionRuntimeStage.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public static OpenshiftProvisionRuntimeStageBuilder builder() {
        return new OpenshiftProvisionRuntimeStageBuilder();

    }

    public static class OpenshiftProvisionRuntimeStageBuilder extends BaseStageBuilder<OpenshiftProvisionRuntimeStage> {

        private OpenshiftProvisionRuntimeStageBuilder() {
            stage = new OpenshiftProvisionRuntimeStage();
        }

        @Override
        public OpenshiftProvisionRuntimeStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public OpenshiftProvisionRuntimeStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder withImage( String image ) {
            stage.setImage( image );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder withLabel( String label ) {
            stage.setLabel( label );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder withInternalPort( String internalPort ) {
            stage.setInternalPort( internalPort );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder withNamespace( String namespace ) {
            stage.setNamespace( namespace );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder withProviderName( String providerName ) {
            stage.setProviderName( providerName );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder withServiceName( String serviceName ) {
            stage.setServiceName( serviceName );
            return this;
        }

        public OpenshiftProvisionRuntimeStageBuilder outRuntimeId( String runtimeIdHolder ) {
            stage.setRuntimeIdHolder( runtimeIdHolder );
            return this;
        }

    }

}
