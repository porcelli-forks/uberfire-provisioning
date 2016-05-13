/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider.base;

import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;

/**
 *
 * @author salaboy
 */
public class WildflyRuntimeConfiguration extends BaseRuntimeConfiguration {

    public void setWarPath(String warPath) {
        getProperties().put("warPath", warPath);
    }

    public String getWarPath() {
        return getProperties().get("warPath");
    }

}
