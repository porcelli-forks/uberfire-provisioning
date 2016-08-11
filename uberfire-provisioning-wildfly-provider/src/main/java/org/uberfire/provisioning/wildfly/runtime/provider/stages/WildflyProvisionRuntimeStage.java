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
import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;
import org.uberfire.provisioning.pipeline.PipelineDataContext;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderService;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeConfBuilder;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderService;

public class WildflyProvisionRuntimeStage extends BaseStage {

    private String providerName;
    private String warPath;
    private String appContext;

    // IN Holders
    private String providerNameHolder;
    private String warPathHolder;
    private String appContextHolder;

    // Out Holder
    private String runtimeIdHolder;

    public WildflyProvisionRuntimeStage() {
        addRequiredService( RuntimeRegistry.class );
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName( String providerName ) {
        this.providerName = providerName;
    }

    public String getWarPath() {
        return warPath;
    }

    public void setWarPath( String warPath ) {
        this.warPath = warPath;
    }

    public String getAppContext() {
        return appContext;
    }

    public void setAppContext( String appContext ) {
        this.appContext = appContext;
    }

    public String getProviderNameHolder() {
        return providerNameHolder;
    }

    public void setProviderNameHolder( String providerNameHolder ) {
        this.providerNameHolder = providerNameHolder;
    }

    public String getWarPathHolder() {
        return warPathHolder;
    }

    public void setWarPathHolder( String warPathHolder ) {
        this.warPathHolder = warPathHolder;
    }

    public String getAppContextHolder() {
        return appContextHolder;
    }

    public void setAppContextHolder( String appContextHolder ) {
        this.appContextHolder = appContextHolder;
    }

    public String getRuntimeIdHolder() {
        return runtimeIdHolder;
    }

    public void setRuntimeIdHolder( String runtimeIdHolder ) {
        this.runtimeIdHolder = runtimeIdHolder;
    }

    @Override
    public void execute( PipelineInstance pipe,
                         PipelineDataContext pipeData ) {

        if ( getWarPath() == null && getWarPathHolder() != null ) {
            setWarPath( (String) pipeData.getData( getWarPathHolder() ) );
        }

        if ( getAppContext() == null && getAppContextHolder() != null ) {
            setAppContext( (String) pipeData.getData( getAppContextHolder() ) );
        }

        if ( getProviderName() == null && getProviderNameHolder() != null ) {
            setProviderName( (String) pipeData.getData( getProviderNameHolder() ) );
        }

        RuntimeConfiguration wildflyRuntimeConfig = WildflyRuntimeConfBuilder.newConfig()
                .setProviderName( getProviderName() )
                .setContext( getAppContext() )
                .setWarPath( getWarPath() )
                .get();

        RuntimeRegistry runtimeRegistry = pipe.getService( RuntimeRegistry.class );
        Wildfly10Provider provider = (Wildfly10Provider) runtimeRegistry.getProvider( getProviderName() );
        org.uberfire.provisioning.runtime.Runtime newWildflyRuntime;
        ProviderService providerService = new Wildfly10ProviderService( provider );
        try {
            newWildflyRuntime = providerService.create( wildflyRuntimeConfig );
            runtimeRegistry.registerRuntime( newWildflyRuntime );
            pipeData.setData( "${" + getRuntimeIdHolder() + "}", newWildflyRuntime.getId() );
        } catch ( ProvisioningException ex ) {
            ex.printStackTrace();
            Logger.getLogger( WildflyProvisionRuntimeStage.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public static WildflyProvisionRuntimeStageBuilder builder() {
        return new WildflyProvisionRuntimeStageBuilder();

    }

    public static class WildflyProvisionRuntimeStageBuilder extends BaseStageBuilder<WildflyProvisionRuntimeStage> {

        private WildflyProvisionRuntimeStageBuilder() {
            stage = new WildflyProvisionRuntimeStage();
        }

        @Override
        public WildflyProvisionRuntimeStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public WildflyProvisionRuntimeStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder withWarPath( String warPath ) {
            stage.setWarPath( warPath );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder withAppContext( String appContext ) {
            stage.setAppContext( appContext );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder withProviderName( String providerName ) {
            stage.setProviderName( providerName );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder inProviderName( String providerNameHolder ) {
            stage.setProviderName( providerNameHolder );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder inWarPath( String warPathHolder ) {
            stage.setWarPathHolder( warPathHolder );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder inAppContext( String appContextHolder ) {
            stage.setAppContextHolder( appContextHolder );
            return this;
        }

        public WildflyProvisionRuntimeStageBuilder outRuntimeId( String runtimeIdHolder ) {
            stage.setRuntimeIdHolder( runtimeIdHolder );
            return this;
        }

    }

}
