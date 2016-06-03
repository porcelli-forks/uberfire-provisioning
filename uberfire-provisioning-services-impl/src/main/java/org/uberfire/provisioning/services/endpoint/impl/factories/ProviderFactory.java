/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.services.endpoint.impl.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;

/**
 *
 * @author salaboy
 */
public class ProviderFactory {

    public static Provider newProvider(ProviderConfiguration config) {
        String provider = config.getProvider();
        try {
            Constructor<?> constructor = Class.forName(provider).getConstructor(ProviderConfiguration.class);
            return (Provider) constructor.newInstance(config);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ProviderFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
