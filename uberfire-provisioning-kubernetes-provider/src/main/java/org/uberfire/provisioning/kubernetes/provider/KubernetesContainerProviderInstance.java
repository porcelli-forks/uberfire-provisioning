/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.kubernetes.provider;

import io.fabric8.kubernetes.api.model.DoneableReplicationController;
import io.fabric8.kubernetes.api.model.DoneableService;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.ClientResource;
import io.fabric8.kubernetes.client.dsl.ClientRollableScallableResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProviderInstance;
import org.uberfire.provisioning.spi.providers.info.ContainerInstanceInfoImpl;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;

/**
 *
 * @author salaboy
 */
public class KubernetesContainerProviderInstance extends BaseContainerProviderInstance {

    private DefaultKubernetesClient kube;

    public KubernetesContainerProviderInstance(ContainerProviderInstanceInfo cpi, ContainerInstanceConfiguration config) {
        super("Kubernetes Client", "kubernetes");
        System.out.println(">>> New KubernetesrContainerProviderInstance Instance... " + this.hashCode());
        this.config = config;
        this.containerProviderInstanceInfo = cpi;

        // If I wanted a custom connection to a custom configured kube client I should use here the information contained in CPI
        kube = new DefaultKubernetesClient();

    }

    @Override
    public ContainerInstanceInfo create() {
        String namespace = config.getProperties().get("namespace");
        String replicationControllerName = config.getProperties().get("replicationController");
        String label = config.getProperties().get("label");
        String image = config.getProperties().get("image");
        String serviceName = config.getProperties().get("serviceName");
        ClientRollableScallableResource<ReplicationController, DoneableReplicationController> resource = kube.replicationControllers().inNamespace("default").withName(replicationControllerName);
        if (resource != null) {
            ReplicationController rc = resource.get();
            if (rc != null) {
                ReplicationControllerStatus status = rc.getStatus();
                Integer replicas = status.getReplicas();
            } else {
                kube.replicationControllers().inNamespace(namespace).createNew()
                        .withNewMetadata().withName(replicationControllerName).addToLabels("app", label).endMetadata()
                        .withNewSpec().withReplicas(1)
                        .withNewTemplate()
                        .withNewMetadata().withName(replicationControllerName).addToLabels("app", label).endMetadata()
                        .withNewSpec()
                        .addNewContainer().withName(label).withImage(image)
                        // .addNewPort().withContainerPort(8080).withHostPort(8080).endPort()
                        .endContainer()
                        .endSpec()
                        .endTemplate()
                        .endSpec().done();
            }
        }
        ClientResource<Service, DoneableService> serviceResource = kube.services().inNamespace(namespace).withName(serviceName);
        Service service = serviceResource.get();
        if (serviceResource != null) {

            if (service != null) {
                ServiceStatus status = service.getStatus();
            } else {
                service = kube.services().inNamespace(namespace).createNew()
                        .withNewMetadata().withName(serviceName).endMetadata()
                        .withNewSpec()
                        .addToSelector("app", "drools")
                        .addNewPort().withPort(80).withNewTargetPort().withIntVal(8080).endTargetPort().endPort()
                        .endSpec()
                        .done();
            }

        }

        final String id = service.getMetadata().getUid();
        System.out.println(">>> ID: " + id);

        containerInstanceInfo = new ContainerInstanceInfoImpl(id, config.getProperties().get("serviceName"), config);

        return containerInstanceInfo;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

}
