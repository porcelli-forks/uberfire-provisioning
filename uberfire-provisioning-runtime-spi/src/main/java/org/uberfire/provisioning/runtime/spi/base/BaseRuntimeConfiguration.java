/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.base;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class BaseRuntimeConfiguration implements RuntimeConfiguration {

    private Map<String, String> properties = new HashMap<String, String>();

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> props) {
        this.properties = props;
    }

}
