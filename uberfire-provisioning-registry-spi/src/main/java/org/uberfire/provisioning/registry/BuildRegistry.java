/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.registry;

import java.util.List;
import org.uberfire.provisioning.build.spi.Binary;

/**
 *
 * @author salaboy
 */
public interface BuildRegistry {
    public void registerBinary(Binary binary);
    
    public List<Binary> getAllBinaries();
}
