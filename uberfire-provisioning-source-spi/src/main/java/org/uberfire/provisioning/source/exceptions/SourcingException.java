/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.exceptions;

/**
 *
 * @author salaboy
 */
public class SourcingException extends Exception {

    public SourcingException(String message) {
        super(message);
    }

    public SourcingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
