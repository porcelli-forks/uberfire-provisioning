/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers.base;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class BaseProviderConfiguration implements ProviderConfiguration {

    private Map<String, String> properties = new HashMap<>();
    private String name;
    private String provider;

    public BaseProviderConfiguration(String name, String provider) {
        this.name = name;
        this.provider = provider;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> props) {
        this.properties = props;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public void setProvider(String provider) {
        this.provider = provider;
    }

}
