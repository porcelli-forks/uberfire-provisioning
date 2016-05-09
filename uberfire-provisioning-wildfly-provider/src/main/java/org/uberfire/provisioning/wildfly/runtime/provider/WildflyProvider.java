/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import javax.xml.bind.annotation.XmlTransient;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.exception.ProvisioningException;
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
    @JsonIgnore
    private WildflyRemoteClient wildfly;

    public WildflyProvider(ProviderConfiguration config) {
        this(config, new WildflyProviderType());

    }

    public WildflyProvider(ProviderConfiguration config, ProviderType type) {
        super(config.getName(), type);
        /*
         I should check here for the required configuration parameters for the provider
         */
        wildfly = new WildflyRemoteClient();
        this.config = config;
    }

    @Override
    public Runtime create(RuntimeConfiguration runtimeConfig) throws ProvisioningException {
        /*
         I should check here for the required configuration parameters for the runtime
         */

        String warPath = runtimeConfig.getProperties().get("warPath");
        String user = config.getProperties().get("user");
        String password = config.getProperties().get("password");
        String host = config.getProperties().get("host");
        String port = config.getProperties().get("port");

        int result = wildfly.deploy(user, password, host, new Integer(port), warPath);
        if (result != 200) {
            throw new ProvisioningException("Deployment to Wildfly Failed with error code: " + result);
        }
        String id = UUID.randomUUID().toString();
        String shortId = id.substring(0, 12);

        return new WildflyRuntime(shortId, runtimeConfig, this);
    }

    @Override
    public void destroy(String runtimeId) throws ProvisioningException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public WildflyRemoteClient getWildfly() {
        return wildfly;
    }

}
