/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.local.runtime.provider;

/**
 *
 * @author salaboy
 */
public class LocalRuntimeConfBuilder {

    private static LocalRuntimeConfBuilder instance;
    private static LocalRuntimeConfiguration config;

    private LocalRuntimeConfBuilder() {
    }

    public static LocalRuntimeConfBuilder newConfig() {
        instance = new LocalRuntimeConfBuilder();
        config = new LocalRuntimeConfiguration();
        return instance;
    }

    public LocalRuntimeConfBuilder setJar(String jar) {
        config.setJar(jar);
        return instance;
    }
   

    public LocalRuntimeConfiguration get() {
        return config;
    }

}
