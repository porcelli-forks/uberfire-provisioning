/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider;

import java.util.UUID;
import javax.xml.bind.annotation.XmlTransient;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProvider;
import org.uberfire.provisioning.wildfly.util.WildflyRemoteClient;

/**
 *
 * @author salaboy
 */
public class WildflyProvider extends BaseProvider {

    @XmlTransient
    private WildflyRemoteClient wildfly;

    public WildflyProvider(ProviderConfiguration config) {
        this(config, new WildflyProviderType());

    }

    public WildflyProvider(ProviderConfiguration config, ProviderType type) {
        super("Wildfly Client Provider", type);
        System.out.println(">>> New WildflyProvider Instance... " + this.hashCode());
        /*
         I should check here for the required configuration parameters for the provider
         */
        wildfly = new WildflyRemoteClient();
        this.config = config;
    }

    @Override
    public Runtime create(RuntimeConfiguration runtimeConfig) {
        /*
         I should check here for the required configuration parameters for the runtime
         */

        String warPath = runtimeConfig.getProperties().get("warPath");
        String user = config.getProperties().get("user");
        String password = config.getProperties().get("password");
        String host = config.getProperties().get("host");
        String port = config.getProperties().get("port");

        System.out.println("Creating container with user: " + user);
        System.out.println("Creating container with password: " + password);
        System.out.println("Creating container with warPath: " + warPath);
        System.out.println("Creating container with host: " + host);
        System.out.println("Creating container with port: " + port);

        wildfly.deploy(user, password, host, new Integer(port), warPath);
        String id = UUID.randomUUID().toString();
        String shortId = id.substring(0, 12);

        return new WildflyRuntime(shortId, runtimeConfig, this);
    }

    @Override
    public void destroy(String runtimeId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public WildflyRemoteClient getWildfly() {
        return wildfly;
    }

}
