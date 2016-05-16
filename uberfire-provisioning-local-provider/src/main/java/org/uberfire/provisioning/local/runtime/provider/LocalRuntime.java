/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.local.runtime.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.RuntimeInfo;
import org.uberfire.provisioning.runtime.spi.RuntimeState;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntime;
import org.uberfire.provisioning.runtime.spi.providers.Provider;

/**
 *
 * @author salaboy
 */
public class LocalRuntime extends BaseRuntime {

    private Process p;
    
    public LocalRuntime(String id, RuntimeConfiguration config, Provider provider) {
        super(id, config, provider);
        if (!(provider instanceof LocalProvider)) {
            throw new IllegalArgumentException("Wrong provider! set: " + provider.getClass() + " expected: LocalProvider");
        }

    }

    @Override
    public void start() {
        try {
            p = Runtime.getRuntime().exec("java -jar " + config.getProperties().get("jar"));
            new Thread(new Runnable() {
                public void run() {
                    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;

                    try {
                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            
        } catch (IOException ex) {
            Logger.getLogger(LocalRuntime.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void stop() {
        Process destroyForcibly = p.destroyForcibly();
    }

    @Override
    public void restart() {

    }

    @Override
    public RuntimeInfo getInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuntimeState getState() {

        return null;
    }

}
