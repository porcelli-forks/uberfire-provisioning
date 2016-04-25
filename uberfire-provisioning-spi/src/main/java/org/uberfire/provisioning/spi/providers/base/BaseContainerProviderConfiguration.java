/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.spi.providers.base;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.uberfire.provisioning.spi.providers.ContainerProviderConfiguration;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class BaseContainerProviderConfiguration implements ContainerProviderConfiguration {

    private Map<String, String> properties;

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> props) {
        this.properties = props;
    }
}
