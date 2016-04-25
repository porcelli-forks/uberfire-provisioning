/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.spi.providers;

/**
 *
 * @author salaboy This class provides the definition for a ContainerProvider.
 * The provider will be in charge of allowing us to create new
 * ContainerInstanceProviders
 */
public interface ContainerProvider {

    public String getProviderName();

    public String getVersion();


}
