/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.provider;

import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.base.BaseContainer;


/**
 *
 * @author salaboy
 */
public class WildflyContainer extends BaseContainer {

    public WildflyContainer(String name, ContainerInstanceConfiguration conf) {
        super(name, conf);
        System.out.println(" >>> New WildflyContainer Instance: "+ this.hashCode());
    }
    
    
}
