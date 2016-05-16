/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.local.runtime.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderType;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@Local
public class LocalProviderType extends BaseProviderType {

    
    public LocalProviderType() {
        super("local", "1.0", LocalProvider.class);
    }

}
