/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers;

/**
 *
 * @author salaboy 
 * This class provides the definition for a ProviderType.
 * Different provider types can be implemented and discovered at runtime.
 */
public interface ProviderType {

    public String getProviderTypeName();

    public String getVersion();

    public Class getProvider();

}
