/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;

/**
 *
 * @author salaboy
 */
public class DockerProviderConfiguration extends BaseProviderConfiguration {

    /*
     * This constructor shouldn't be used, The use of DockerProviderConfiguration(String name)
     * is strictly recommended. This constructor is here just for the serializers to work correctly
    */
    public DockerProviderConfiguration() {
        super("", new DockerProviderType().getProvider().getName());
    }

    public DockerProviderConfiguration(String name) {
        super(name, new DockerProviderType().getProvider().getName());
    }

}
