/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.uberfire.provisioning.build.spi.Binary;
import org.uberfire.provisioning.registry.BuildRegistry;

/**
 *
 * @author salaboy This is a not thread-safe implementation for local testing. A
 * more robust and distributed implementation should be provided for real usecases
 */
public class InMemoryBuildRegistry implements BuildRegistry {

    private final Map<String, Binary> binariesByName;

    public InMemoryBuildRegistry() {
        binariesByName = new HashMap<>();
        
    }

    @Override
    public void registerBinary(Binary binary) {
        binariesByName.put(binary.getName(), binary);
    }

    @Override
    public List<Binary> getAllBinaries() {
        return new ArrayList<>(binariesByName.values());
    }

    
    
}
