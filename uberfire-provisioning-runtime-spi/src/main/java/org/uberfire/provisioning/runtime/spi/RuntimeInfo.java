/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi;

/**
 *
 * @author salaboy This class represent the Container instance information,
 * which might describe how to talk with the application/image instance and
 * which features are provided
 */
public interface RuntimeInfo {

    public String getId();

    public String getName();

    public RuntimeConfiguration getConfig();
}
