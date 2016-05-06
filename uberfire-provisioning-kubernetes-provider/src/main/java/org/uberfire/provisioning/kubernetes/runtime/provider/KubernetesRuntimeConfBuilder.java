/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.kubernetes.runtime.provider;

/**
 *
 * @author salaboy
 */
public class KubernetesRuntimeConfBuilder {

    private static KubernetesRuntimeConfBuilder instance;
    private static KubernetesRuntimeConfiguration config;

    private KubernetesRuntimeConfBuilder() {
    }

    public static KubernetesRuntimeConfBuilder newConfig() {
        instance = new KubernetesRuntimeConfBuilder();
        config = new KubernetesRuntimeConfiguration();
        return instance;
    }

    public KubernetesRuntimeConfBuilder setNamespace(String namespace) {
        config.setNamespace(namespace);
        return instance;
    }

    public KubernetesRuntimeConfBuilder setServiceName(String serviceName) {
        config.setServiceName(serviceName);
        return instance;
    }

    public KubernetesRuntimeConfBuilder setReplicationController(String replicationController) {
        config.setReplicationController(replicationController);
        return instance;
    }

    public KubernetesRuntimeConfBuilder setLabel(String label) {
        config.setLabel(label);
        return instance;
    }

    public KubernetesRuntimeConfBuilder setImage(String image) {
        config.setImage(image);
        return instance;
    }

    public KubernetesRuntimeConfiguration get() {
        return config;
    }

}
