/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider.base;

/**
 *
 * @author salaboy
 */
public class WildflyProviderConfBuilder {
    
    private static WildflyProviderConfBuilder instance;
    private static WildflyProviderConfiguration config;
    
    private WildflyProviderConfBuilder() {
        
    }
    
    public static WildflyProviderConfBuilder newConfig(String providerName) {
        instance = new WildflyProviderConfBuilder();
        config = new WildflyProviderConfiguration(providerName);
        return instance;
    }
    
    public WildflyProviderConfBuilder setName(String name) {
        config.setName(name);
        return instance;
    }
    
    public WildflyProviderConfBuilder setHost(String host) {
        config.setHost(host);
        return instance;
    }
    
    public WildflyProviderConfBuilder setPort(String port) {
        config.setManagementPort(port);
        return instance;
    }
    
    public WildflyProviderConfBuilder setUser(String user) {
        config.setUser(user);
        return instance;
    }
    
    public WildflyProviderConfBuilder setPassword(String password) {
        config.setPassword(password);
        return instance;
    }
    
    public WildflyProviderConfiguration get() {
        return config;
    }
    
}
