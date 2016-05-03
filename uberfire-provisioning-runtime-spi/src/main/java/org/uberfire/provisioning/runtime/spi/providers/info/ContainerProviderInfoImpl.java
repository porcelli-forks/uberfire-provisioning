/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers.info;

/**
 *
 * @author salaboy
 */
public class ContainerProviderInfoImpl implements ContainerProviderInfo {

    private String providerName;
    private String version;

    public ContainerProviderInfoImpl() {
    }

    public ContainerProviderInfoImpl(String providerName, String version) {
        this.providerName = providerName;
        this.version = version;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ContainerProviderInfoImpl{" + "providerName=" + providerName + ", version=" + version + '}';
    }

}
