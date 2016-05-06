/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;

/**
 *
 * @author salaboy
 */
public class DockerRuntimeConfiguration extends BaseRuntimeConfiguration {

    public void setImage(String image) {
        getProperties().put("image", image);
    }

    public String getImage() {
        return getProperties().get("image");
    }

    public void setPull(boolean pull) {
        getProperties().put("pull", Boolean.toString(pull));
    }

    public boolean getPull() {
        return Boolean.getBoolean(getProperties().get("pull"));
    }
}
