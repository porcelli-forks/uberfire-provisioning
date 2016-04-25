/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.provider;

import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProviderInstance;
import org.uberfire.provisioning.spi.providers.info.ContainerInstanceInfoImpl;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;
import org.uberfire.provisioning.wildfly.util.WildflyRemoteClient;

/**
 *
 * @author salaboy
 */
public class WildflyContainerProviderInstance extends BaseContainerProviderInstance {

    private WildflyRemoteClient client;

    public WildflyContainerProviderInstance(ContainerProviderInstanceInfo cpi, ContainerInstanceConfiguration config) {
        super("Wildfly Client Provider", "wildfly");
        System.out.println(">>> New WildflyContainerProviderInstance Instance... " + this.hashCode());

        this.config = config;
        this.containerProviderInstanceInfo = cpi;

    }

    @Override
    public ContainerInstanceInfo create() {

        String appName = config.getProperties().get("name");
        String warPath = config.getProperties().get("warPath");
        String target = config.getProperties().get("target");
        String user = config.getProperties().get("user");
        String password = config.getProperties().get("password");
        String host = config.getProperties().get("host");
        String port = config.getProperties().get("port");

        System.out.println("Creating container with user: " + user);
        System.out.println("Creating container with password: " + password);
        System.out.println("Creating container with target: " + target);
        System.out.println("Creating container with warPath: " + warPath);

        client = new WildflyRemoteClient();
        client.deploy(user, password, host, new Integer(port), warPath);

        containerInstanceInfo = new ContainerInstanceInfoImpl(appName, appName, config);

        return containerInstanceInfo;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

}
