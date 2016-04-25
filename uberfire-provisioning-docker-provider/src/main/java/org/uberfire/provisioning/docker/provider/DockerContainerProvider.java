/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProvider;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@DockerProvider
public class DockerContainerProvider extends BaseContainerProvider {

    
    public DockerContainerProvider() {
        super("docker", "1.9.1");
        System.out.println(" >>> New DockerContainerProvider Instance: "+ this.hashCode());
    }

}
