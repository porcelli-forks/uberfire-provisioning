/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ContainerState;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.RuntimeInfo;
import org.uberfire.provisioning.runtime.spi.RuntimeState;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntime;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeState;
import org.uberfire.provisioning.runtime.spi.providers.Provider;

/**
 *
 * @author salaboy
 */
public class DockerRuntime extends BaseRuntime {

    public DockerRuntime(String id, RuntimeConfiguration config, Provider provider) {
        super(id, config, provider);
        if (!(provider instanceof DockerProvider)) {
            throw new IllegalArgumentException("Wrong provider! set: " + provider.getClass() + " expected: DockerProvider");
        }

    }

    @Override
    public void start() {
        try {
            ((DockerProvider) provider).getDocker().startContainer(getId());
        } catch (DockerException | InterruptedException ex) {
            Logger.getLogger(DockerProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void stop() {

        try {
            ((DockerProvider) provider).getDocker().stopContainer(getId(), 0);
        } catch (DockerException | InterruptedException ex) {
            Logger.getLogger(DockerProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void restart() {

        try {
            ((DockerProvider) provider).getDocker().restartContainer(getId());
        } catch (DockerException | InterruptedException ex) {
            Logger.getLogger(DockerProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public RuntimeInfo getInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuntimeState getState() {
        try {
            ContainerInfo containerInfo = ((DockerProvider) provider).getDocker().inspectContainer(getId());
            ContainerState state = containerInfo.state();
            return new BaseRuntimeState(state.running(), state.startedAt());
        } catch (DockerException | InterruptedException ex) {
            Logger.getLogger(DockerRuntime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
