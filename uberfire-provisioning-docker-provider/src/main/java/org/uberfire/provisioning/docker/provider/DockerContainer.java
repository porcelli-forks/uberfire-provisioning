/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.provider;

import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.base.BaseContainer;


/**
 *
 * @author salaboy
 */
public class DockerContainer extends BaseContainer {

    public DockerContainer(String name, ContainerInstanceConfiguration conf) {
        super(name, conf);
        System.out.println(" >>> New DockerContainer Instance: "+ this.hashCode());
    }
    
    
}
