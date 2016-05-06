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

    public DockerProviderConfiguration(String name) {
        super(name, new DockerProviderType().getProviderTypeName());
    }

}
