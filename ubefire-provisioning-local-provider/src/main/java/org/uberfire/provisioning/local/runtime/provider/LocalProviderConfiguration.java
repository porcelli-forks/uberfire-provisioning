/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.local.runtime.provider;

import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;

/**
 *
 * @author salaboy
 */
public class LocalProviderConfiguration extends BaseProviderConfiguration {

    public LocalProviderConfiguration(String name) {
        super(name, new LocalProviderType().getProvider().getName());
    }

}
