/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source;

/**
 *
 * @author salaboy
 */
public interface Source {

    public String getSource(String repository, String pathTo) throws Exception;
}
