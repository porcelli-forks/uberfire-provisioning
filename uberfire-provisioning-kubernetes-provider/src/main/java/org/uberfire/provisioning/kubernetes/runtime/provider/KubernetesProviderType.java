/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.kubernetes.runtime.provider;

import javax.enterprise.context.ApplicationScoped;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderType;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
@Kubernetes
public class KubernetesProviderType extends BaseProviderType {

    public KubernetesProviderType() {
        super("kubernetes", "1");
        System.out.println(" >>> New KubernetesProviderType: " + this.hashCode());
    }

}
