/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry.local.tests;

import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderType;

/**
 *
 * @author salaboy
 */
public class MyProviderType extends BaseProviderType {
    
    public MyProviderType(String providerName, String version) {
        super(providerName, version, MyProvider.class);
    }
    
    public MyProviderType() {
        this("my provider", "1.0");
    }
    
}
