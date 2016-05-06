/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.runtime.provider;

/**
 *
 * @author salaboy
 */
public class WildflyRuntimeConfBuilder {

    private static WildflyRuntimeConfBuilder instance;
    private static WildflyRuntimeConfiguration config;

    private WildflyRuntimeConfBuilder() {

    }

    public static WildflyRuntimeConfBuilder newConfig() {
        instance = new WildflyRuntimeConfBuilder();
        config = new WildflyRuntimeConfiguration();
        return instance;
    }

    public WildflyRuntimeConfBuilder setWarPath(String warPath) {
        config.setWarPath(warPath);
        return instance;
    }

    public WildflyRuntimeConfiguration get() {
        return config;
    }

}
