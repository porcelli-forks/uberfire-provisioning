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

package org.uberfire.provisioning.openshift.runtime.provider;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.ContainerState;
import io.fabric8.kubernetes.api.model.ContainerStateRunning;
import io.fabric8.kubernetes.api.model.ContainerStateTerminated;
import io.fabric8.kubernetes.api.model.ContainerStateWaiting;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.DoneableReplicationController;
import io.fabric8.kubernetes.api.model.DoneableService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.ClientResource;
import io.fabric8.kubernetes.client.dsl.ClientRollableScallableResource;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.api.model.RouteList;
import io.fabric8.openshift.api.model.RouteSpec;
import io.fabric8.openshift.client.OpenShiftClient;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.base.BaseRuntimeEndpoint;
import org.uberfire.provisioning.runtime.providers.ProviderService;

public class OpenshiftRuntimeService implements RuntimeService {

    protected ProviderService providerService;
    protected org.uberfire.provisioning.runtime.Runtime runtime;

    public OpenshiftRuntimeService( ProviderService providerService,
                                    Runtime runtime ) {
        this.providerService = providerService;
        this.runtime = runtime;
        if ( !( providerService instanceof OpenshiftProviderService ) ) {
            throw new IllegalArgumentException( "Wrong provider service! set: " + providerService.getClass() + " expected: KubernetesProvider" );
        }
    }

    @Override
    public void start() {
        System.out.println( "Doing nothing for now ..." );
    }

    @Override
    public void stop() {
        String replicationControllerName = runtime.getConfig().getProperties().get( "replicationController" );
        ClientRollableScallableResource<ReplicationController, DoneableReplicationController> resource
                = ( (OpenshiftProviderService) providerService ).getKubernetesClient().replicationControllers().inNamespace( "default" )
                .withName( replicationControllerName );
        resource.scale( 0 );
    }

    @Override
    public void restart() {
        System.out.println( "Doing nothing for now ..." );
    }

    @Override
    public void refresh() {
        String serviceName = runtime.getConfig().getProperties().get( "serviceName" );
        String namespace = runtime.getConfig().getProperties().get( "namespace" );

        ClientResource<Service, DoneableService> serviceResource =
                ( (OpenshiftProviderService) providerService ).getKubernetesClient().services()
                        .inNamespace( namespace ).withName( serviceName );

        try {
            if ( serviceResource != null ) {
                Service service = serviceResource.get();
                if ( service != null ) {
                    ServiceStatus status = service.getStatus();
                    Map<String, Object> additionalProperties = status.getAdditionalProperties();
                    System.out.println( ">>> Additional Properties: " );
                    for ( String key : additionalProperties.keySet() ) {
                        System.out.println( "Key: " + key );
                        System.out.println( "Value: " + additionalProperties.get( key ) );
                    }
                    ServiceSpec spec = service.getSpec();
                    String clusterIP = spec.getClusterIP();
                    System.out.println( "Cluster IP: " + clusterIP );
                    List<String> externalIPs = spec.getExternalIPs();
                    for ( String ip : externalIPs ) {
                        System.out.println( "External IP: " + ip );
                    }

                    List<ServicePort> ports = spec.getPorts();
                    for ( ServicePort p : ports ) {
                        System.out.println( "Service Port: (" + p.getName() + ") " + p.getPort() );
                    }
                }
            }
        } catch ( Exception ex ) {
        }

        runtime.setInfo( new OpenshiftRuntimeInfo( runtime.getId(), runtime.getId(), runtime.getConfig() ) );

        String selector = runtime.getConfig().getProperties().get( "label" );
        FilterWatchListDeletable<Pod, PodList, Boolean, Watch, Watcher<Pod>> podResource =
                ( (OpenshiftProviderService) providerService ).getKubernetesClient().pods().withLabel( "app", selector );
        PodList list = podResource.list();
        boolean empty = list.getItems().isEmpty();
        if ( empty ) {
            System.out.println( ">>> No PODS to show! " );
            runtime.setState( new OpenshiftRuntimeState( "not running", "NA" ) );
        }

        for ( Pod pod : list.getItems() ) {
            System.out.println( ">>>>  POD: " + pod.toString() );
            String startTime = pod.getStatus().getStartTime();
            System.out.println( "Start Time Pod: " + startTime );
            List<ContainerStatus> containerStatuses = pod.getStatus().getContainerStatuses();
            String state = "";
            for ( ContainerStatus cs : containerStatuses ) {
                Boolean ready = cs.getReady();
                System.out.println( "Is CS ready : " + ready );
                ContainerState lastState = cs.getState();
                System.out.println( "Container State state: " + lastState );
                ContainerStateRunning running = lastState.getRunning();
                System.out.println( "Is Running? " + running );
                if ( running != null ) {
                    String startedAt = running.getStartedAt();
                    System.out.println( "Last state Running?? started at " + startedAt );
                }
                ContainerStateTerminated terminated = lastState.getTerminated();
                System.out.println( "Is Terminated? " + terminated );
                if ( terminated != null ) {
                    String reason = terminated.getReason();

                    String finishedAt = terminated.getFinishedAt();
                    Integer exitCode = terminated.getExitCode();
                    System.out.println( "Last state Terminated?? reason  " + reason + " - finishedAt: " + finishedAt + " - exit code: " + exitCode );
                }
                ContainerStateWaiting waiting = lastState.getWaiting();
                System.out.println( "Is Waiting? " + waiting );
                if ( waiting != null ) {
                    String messageWaiting = waiting.getMessage();
                    String reason1 = waiting.getReason();
                    System.out.println( "Last state Waiting?? message  " + messageWaiting + " - reason: " + reason1 );
                }

            }
            runtime.setState( new OpenshiftRuntimeState( state, startTime ) );

        }

        serviceName = runtime.getConfig().getProperties().get( "serviceName" );
        OpenShiftClient osClient = ( (OpenshiftProviderService) providerService ).getKubernetesClient().adapt( OpenShiftClient.class );
        FilterWatchListDeletable<Route, RouteList, Boolean, Watch, Watcher<Route>> routeResources =
                osClient.routes().withLabel( "name", serviceName );
        List<Route> items = routeResources.list().getItems();
        RouteSpec spec = items.get( 0 ).getSpec();
        String host = spec.getHost();

        Integer port = 80;
        if ( spec.getPort() != null ) {
            port = spec.getPort().getTargetPort().getIntVal();
        }

        String context = runtime.getConfig().getProperties().get( "context" );
        runtime.setEndpoint( new BaseRuntimeEndpoint( host, port, context ) );

    }

    @Override
    public ProviderService getProviderService() {
        return providerService;
    }

    @Override
    public void setProviderService( ProviderService providerService ) {
        this.providerService = providerService;
    }

    @Override
    public Runtime getRuntime() {
        return runtime;
    }

    @Override
    public void setRuntime( Runtime runtime ) {
        this.runtime = runtime;
    }

}
