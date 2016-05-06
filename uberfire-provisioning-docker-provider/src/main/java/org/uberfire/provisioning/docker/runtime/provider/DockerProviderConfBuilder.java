/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

/**
 *
 * @author salaboy
 */
public class DockerProviderConfBuilder {

    private static DockerProviderConfBuilder instance;
    private static DockerProviderConfiguration config;

    private DockerProviderConfBuilder() {
    }

    public static DockerProviderConfBuilder newConfig(String providerName) {
        instance = new DockerProviderConfBuilder();
        config = new DockerProviderConfiguration(providerName);
        return instance;
    }

    public DockerProviderConfiguration get() {
        return config;
    }
}
