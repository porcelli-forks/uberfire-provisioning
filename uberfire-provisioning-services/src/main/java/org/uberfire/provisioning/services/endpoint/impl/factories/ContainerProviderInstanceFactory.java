/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.services.endpoint.impl.factories;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.docker.provider.DockerContainerProviderInstance;
import org.uberfire.provisioning.kubernetes.provider.KubernetesContainerProviderInstance;
import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.providers.ContainerProviderInstance;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;
import org.uberfire.provisioning.wildfly.provider.WildflyContainerProviderInstance;

/**
 *
 * @author salaboy
 */
public class ContainerProviderInstanceFactory {

    /*
     * @TODO: refactor this to be smarter
    */
    public static ContainerProviderInstance newContainerProviderInstance(ContainerProviderInstanceInfo cpi, ContainerInstanceConfiguration cic) {
        String providerName = cpi.getProviderName();
        switch (providerName) {
            case "docker":
                DockerContainerProviderInstance dockerContainerProviderInstance = new DockerContainerProviderInstance(cpi, cic);
                try {
                    dockerContainerProviderInstance.create();
                } catch (DockerCertificateException | DockerException | InterruptedException ex) {
                    Logger.getLogger(ContainerProviderInstanceFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
                return dockerContainerProviderInstance;
            case "kubernetes":
                KubernetesContainerProviderInstance kubeContainerProviderInstance = new KubernetesContainerProviderInstance(cpi, cic);
                kubeContainerProviderInstance.create();
                return kubeContainerProviderInstance;
            case "wildfly":
                WildflyContainerProviderInstance wildflyContainerProviderInstance = new WildflyContainerProviderInstance(cpi, cic);
                wildflyContainerProviderInstance.create();
                return wildflyContainerProviderInstance;
            default:
                break;
        }
//        else if(providerName.equals("was")){
//            WASContainerProviderInstance wasContainerProviderInstance = new WASContainerProviderInstance(cpi, cic);
//            wasContainerProviderInstance.create();
//            return wasContainerProviderInstance;
//        }
        return null;
    }
    
}
