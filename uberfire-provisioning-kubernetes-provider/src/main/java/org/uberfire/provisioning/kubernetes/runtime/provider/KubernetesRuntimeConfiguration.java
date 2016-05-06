/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.kubernetes.runtime.provider;

import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;

/**
 *
 * @author salaboy
 */
public class KubernetesRuntimeConfiguration extends BaseRuntimeConfiguration {

    public void setNamespace(String namespace) {
        getProperties().put("namespace", namespace);
    }

    public void setReplicationController(String replicationController) {
        getProperties().put("replicationController", replicationController);
    }

    public void setLabel(String label) {
        getProperties().put("label", label);
    }

    public void setServiceName(String serviceName) {
        getProperties().put("serviceName", serviceName);
    }

    public void setImage(String image) {
        getProperties().put("image", image);
    }
    
    

}
