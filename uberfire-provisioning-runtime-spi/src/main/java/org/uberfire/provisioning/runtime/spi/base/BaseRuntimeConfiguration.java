/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;

/**
 *
 * @author salaboy
 */
public class BaseRuntimeConfiguration implements RuntimeConfiguration {

    @JsonIgnore
    private Map<String, String> properties = new HashMap<String, String>();

    private String providerName;

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> props) {
        this.properties = props;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

}
