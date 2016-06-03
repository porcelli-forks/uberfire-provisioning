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
 * 
 * The Source interface provides all the methods to obtain and manage the source code that 
 *  will be used by the other services. 
 */
public interface Source {
    /*
    * Retrieve the source code from a Repository 
    * returns the location (path) of the obtained code
    * @throws SourcingException if the repository cannot  be located or the code cannot be retrieved
    * @param repository a source code Repository to use as Source for our projects
    * @return a String with the path to the retrieved source code
    * @see Repository
    */
    public String getSource(Repository repository) throws SourcingException;
   
    /*
    * Clean the source code obtained from a repository 
    * @throws SourcingException if there is an issue trying to clean the repository local path
    * @param repository a repository to clean
    * @return true if the source was cleaned, false if it didn't exist
    * @see Repository
    */
    public boolean cleanSource(String sourcePath) throws SourcingException;
    
    
}
