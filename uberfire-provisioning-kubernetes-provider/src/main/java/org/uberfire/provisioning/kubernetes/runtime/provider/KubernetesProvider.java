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

package org.uberfire.provisioning.kubernetes.runtime.provider;

import javax.xml.bind.annotation.XmlTransient;

import io.fabric8.kubernetes.api.model.DoneableReplicationController;
import io.fabric8.kubernetes.api.model.DoneableService;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.ClientResource;
import io.fabric8.kubernetes.client.dsl.ClientRollableScallableResource;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.runtime.providers.base.BaseProvider;

import static java.lang.System.*;

/**
 * @author salaboy
 */
public class KubernetesProvider extends BaseProvider {

    @XmlTransient
    private DefaultKubernetesClient kubernetes;

    public KubernetesProvider() {
    }

    
    public KubernetesProvider( ProviderConfiguration config,
                               ProviderType type ) {
        super( config.getName(), type );
        this.config = config;

        // If I wanted a custom connection to a custom configured kube client I should use here the information contained in CPI
        kubernetes = new DefaultKubernetesClient();

    }

    public KubernetesProvider( ProviderConfiguration config ) {
        this( config, new KubernetesProviderType() );

    }

    @Override
    public Runtime create( RuntimeConfiguration runtimeConfig ) throws ProvisioningException {
        String namespace = runtimeConfig.getProperties().get( "namespace" );
        String replicationControllerName = runtimeConfig.getProperties().get( "replicationController" );
        String label = runtimeConfig.getProperties().get( "label" );
        String image = runtimeConfig.getProperties().get( "image" );
        String serviceName = runtimeConfig.getProperties().get( "serviceName" );
        ClientRollableScallableResource<ReplicationController, DoneableReplicationController> resource = kubernetes.replicationControllers().inNamespace( "default" ).withName( replicationControllerName );
        if ( resource != null ) {
            try {
                ReplicationController rc = resource.get();
                if ( rc != null ) {
                    ReplicationControllerStatus status = rc.getStatus();
                    Integer replicas = status.getReplicas();
                } else {
                    kubernetes.replicationControllers().inNamespace( namespace ).createNew()
                            .withNewMetadata().withName( replicationControllerName ).addToLabels( "app", label ).endMetadata()
                            .withNewSpec().withReplicas( 1 )
                            .withNewTemplate()
                            .withNewMetadata().withName( replicationControllerName ).addToLabels( "app", label ).endMetadata()
                            .withNewSpec()
                            .addNewContainer().withName( label ).withImage( image )
                            // .addNewPort().withContainerPort(8080).withHostPort(8080).endPort()
                            .endContainer()
                            .endSpec()
                            .endTemplate()
                            .endSpec().done();
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
                throw new ProvisioningException( "Error provisioning to Kubernetes: " + ex.getMessage() );
            }

        }

        ClientResource<Service, DoneableService> serviceResource = kubernetes.services().inNamespace( namespace ).withName( serviceName );
        try {
            Service service = serviceResource.get();
            if ( serviceResource != null ) {

                if ( service != null ) {
                    ServiceStatus status = service.getStatus();
                } else {
                    service = kubernetes.services().inNamespace( namespace ).createNew()
                            .withNewMetadata().withName( serviceName ).endMetadata()
                            .withNewSpec()
                            .addToSelector( "app", label )
                            .addNewPort().withPort( 80 ).withNewTargetPort().withIntVal( 8080 ).endTargetPort().endPort()
                            .endSpec()
                            .done();
                }

            }
            final String id = service.getMetadata().getUid();
            out.println( ">>> ID: " + id );

            return new KubernetesRuntime( id, runtimeConfig, this );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            throw new ProvisioningException( "Error provisioning to Kubernetes: " + ex.getMessage() );
        }

    }

    @Override
    public void destroy( String runtimeId ) throws ProvisioningException {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    public DefaultKubernetesClient getKubernetes() {
        return kubernetes;
    }

}
