/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.local.runtime.provider;

import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;

/**
 *
 * @author salaboy
 */
public class LocalRuntimeConfiguration extends BaseRuntimeConfiguration {

    public void setJar(String jar) {
        getProperties().put("jar", jar);
    }

    public String getJar() {
        return getProperties().get("jar");
    }

   
}
