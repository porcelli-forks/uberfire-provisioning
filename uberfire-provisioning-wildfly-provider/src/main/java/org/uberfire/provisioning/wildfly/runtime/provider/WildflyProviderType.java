/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderType;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@Wildfly
public class WildflyProviderType extends BaseProviderType {

    public WildflyProviderType() {
        super("wildfly", "10.0.0");
        System.out.println(" >>> New WildflyProviderType Instance: " + this.hashCode());
    }

}
