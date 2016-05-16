/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.kubernetes.runtime.provider;

import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;

/**
 *
 * @author salaboy
 */
public class KubernetesProviderConfiguration extends BaseProviderConfiguration {

     /*
     * This constructor shouldn't be used, The use of KubernetesProviderConfiguration(String name)
     * is strictly recommended. This constructor is here just for the serializers to work correctly
     */
    public KubernetesProviderConfiguration() {
        super("", new KubernetesProviderType().getProvider().getName());
    }

    public KubernetesProviderConfiguration(String name) {
        super(name, new KubernetesProviderType().getProvider().getName());
    }

    
}
