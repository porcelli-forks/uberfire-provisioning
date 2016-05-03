/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi;

import org.uberfire.provisioning.runtime.spi.providers.Provider;

/**
 *
 * @author salaboy
 *
 * This class represent a Docker Image running or a WAR deployed into a server
 *
 */

public interface Runtime {

    public String getId();

    public void start();

    public void stop();

    public void restart();

    public RuntimeInfo getInfo();

    public RuntimeState getState();

    public Provider getProvider();

    public RuntimeConfiguration getConfig();

}
