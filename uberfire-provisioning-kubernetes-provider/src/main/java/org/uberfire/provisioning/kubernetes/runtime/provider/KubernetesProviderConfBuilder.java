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
public class KubernetesProviderConfBuilder {

    private static KubernetesProviderConfBuilder instance;
    private static KubernetesProviderConfiguration config;

    private KubernetesProviderConfBuilder() {
    }

    public static KubernetesProviderConfBuilder newConfig(String providerName) {
        instance = new KubernetesProviderConfBuilder();
        config = new KubernetesProviderConfiguration(providerName);
        return instance;
    }

    public KubernetesProviderConfiguration get() {
        return config;
    }
}
