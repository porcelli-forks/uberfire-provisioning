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
public class DockerRuntimeConfBuilder {

    private static DockerRuntimeConfBuilder instance;
    private static DockerRuntimeConfiguration config;

    private DockerRuntimeConfBuilder() {
    }

    public static DockerRuntimeConfBuilder newConfig() {
        instance = new DockerRuntimeConfBuilder();
        config = new DockerRuntimeConfiguration();
        return instance;
    }

    public DockerRuntimeConfBuilder setImage(String image) {
        config.setImage(image);
        return instance;
    }

    public DockerRuntimeConfBuilder setPull(boolean pull) {
        config.setPull(pull);
        return instance;
    }

    public DockerRuntimeConfiguration get() {
        return config;
    }

}
