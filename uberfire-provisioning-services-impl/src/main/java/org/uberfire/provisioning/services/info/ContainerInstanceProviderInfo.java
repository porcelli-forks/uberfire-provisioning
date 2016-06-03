/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.services.info;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class ContainerInstanceProviderInfo {

    private String name;
    private String provider;
    private Map<String, String> config;

    public ContainerInstanceProviderInfo() {
    }

    public ContainerInstanceProviderInfo(String name, String provider, Map<String, String> config) {
        this.name = name;
        this.provider = provider;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

}
