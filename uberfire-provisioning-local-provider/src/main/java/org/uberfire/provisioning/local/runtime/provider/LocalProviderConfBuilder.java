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
public class LocalProviderConfBuilder {

    private static LocalProviderConfBuilder instance;
    private static LocalProviderConfiguration config;

    private LocalProviderConfBuilder() {
    }

    public static LocalProviderConfBuilder newConfig(String providerName) {
        instance = new LocalProviderConfBuilder();
        config = new LocalProviderConfiguration(providerName);
        return instance;
    }

    public LocalProviderConfiguration get() {
        return config;
    }
}
