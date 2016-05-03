/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi;

import java.util.Date;

/**
 *
 * @author salaboy
 */
public interface RuntimeState {
    boolean isRunning();
    Date getStartedAt();
}
