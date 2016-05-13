/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider.wildly10;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderType;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@Wildfly10
public class Wildfly10ProviderType extends BaseProviderType {

    public Wildfly10ProviderType() {
        super("wildfly", "10.0.0", Wildfly10Provider.class);
    }

}
