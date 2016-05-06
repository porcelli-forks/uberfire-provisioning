/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderType;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@Docker
public class DockerProviderType extends BaseProviderType {

    
    public DockerProviderType() {
        super("docker", "1.9.1");
    }

}
