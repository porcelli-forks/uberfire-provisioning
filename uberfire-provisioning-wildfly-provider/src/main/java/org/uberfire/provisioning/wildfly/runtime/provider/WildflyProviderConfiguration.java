/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider;

import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;

/**
 *
 * @author salaboy
 */
public class WildflyProviderConfiguration extends BaseProviderConfiguration {

    public WildflyProviderConfiguration(String name) {
        super(name, new WildflyProviderType().getProviderTypeName());
    }
    
    public void setHost(String host) {
        getProperties().put("host", host);
    }

    public void setManagementPort(String port) {
        getProperties().put("port", port);
    }

    public void setUser(String user) {
        getProperties().put("user", user);
    }

    public void setPassword(String password) {
        getProperties().put("password", password);
    }

    public String getHost() {
        return getProperties().get("host");
    }

    public String getManagementPort() {
        return getProperties().get("port");
    }

    public String getUser() {
        return getProperties().get("user");
    }

    public String getPassword() {
        return getProperties().get("password");
    }

}
