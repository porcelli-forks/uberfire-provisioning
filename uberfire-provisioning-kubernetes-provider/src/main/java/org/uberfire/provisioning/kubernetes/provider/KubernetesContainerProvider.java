/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.kubernetes.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProvider;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@KubernetesProvider
public class KubernetesContainerProvider extends BaseContainerProvider {

    
    public KubernetesContainerProvider() {
        super("kubernetes", "");
        System.out.println(" >>> New KubernetesContainerProvider Instance: "+ this.hashCode());
    }

}
