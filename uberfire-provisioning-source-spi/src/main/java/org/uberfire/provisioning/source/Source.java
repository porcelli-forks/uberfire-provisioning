/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source;

import org.uberfire.provisioning.source.exceptions.SourcingException;

/**
 *
 * @author salaboy
 */
public interface Source {

    public String getSource(Repository repository) throws SourcingException;
}
