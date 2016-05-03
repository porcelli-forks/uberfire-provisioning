/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.base;

import java.util.Date;
import org.uberfire.provisioning.runtime.spi.RuntimeState;

/**
 *
 * @author salaboy
 */
public class BaseRuntimeState implements RuntimeState {

    private boolean running;
    private Date startedAt;

    public BaseRuntimeState() {
    }

    public BaseRuntimeState(boolean running, Date startedAt) {
        this.running = running;
        this.startedAt = startedAt;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Date getStartedAt() {
        return startedAt;
    }

}
