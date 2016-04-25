/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProvider;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@WildflyProvider
public class WildflyContainerProvider extends BaseContainerProvider {

    public WildflyContainerProvider() {
        super("wildfly", "10.0.0");
        System.out.println(" >>> New WildflyContainerProvider Instance: " + this.hashCode());
    }

}
