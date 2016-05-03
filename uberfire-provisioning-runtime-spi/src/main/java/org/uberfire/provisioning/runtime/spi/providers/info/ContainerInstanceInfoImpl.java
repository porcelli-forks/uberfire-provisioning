/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers.info;

import org.uberfire.provisioning.runtime.spi.RuntimeInfo;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;

/**
 *
 * @author salaboy
 */
public class ContainerInstanceInfoImpl implements RuntimeInfo {

    private String id;
    private String name;
    private RuntimeConfiguration config;

    public ContainerInstanceInfoImpl() {
    }

    public ContainerInstanceInfoImpl(String id, String name, RuntimeConfiguration config) {
        this.id = id;
        this.name = name;
        this.config = config;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RuntimeConfiguration getConfig() {
        return config;
    }

}
