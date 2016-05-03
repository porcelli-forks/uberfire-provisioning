/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlTransient;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProvider;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

/**
 *
 * @author salaboy
 */
public class DockerProvider extends BaseProvider {

    @XmlTransient
    private DockerClient docker;

    public DockerProvider(ProviderConfiguration config) {
        this(config, new DockerProviderType());
    }

    public DockerProvider(ProviderConfiguration config, ProviderType type) {
        super("Docker Client", type);
        System.out.println(">>> New DockerProvider Instance... " + this.hashCode());
        this.config = config;

        try {
            // If I wanted a custom connection to a custom configured docker deamon I should use here the information contained in CPI
            docker = DefaultDockerClient.fromEnv().build();
        } catch (DockerCertificateException ex) {
            Logger.getLogger(DockerProviderType.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Runtime create(RuntimeConfiguration runtimeConfig) throws DockerCertificateException, DockerException, InterruptedException {

        // Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars
        // Pull an image
        // docker.pull(m.getConfiguration().getProperty("name"));
        // Bind container ports to host ports
        final String[] ports = {"8080"};
        final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
//        for (String port : ports) {
//            List<PortBinding> hostPorts = new ArrayList<PortBinding>();
//            hostPorts.add(PortBinding.of("0.0.0.0", port));
//            portBindings.put(port, hostPorts);
//        }

// Bind container port 443 to an automatically allocated available host port.
        List<PortBinding> randomPort = new ArrayList<PortBinding>();
        PortBinding randomPortBinding = PortBinding.randomPort("0.0.0.0");
        System.out.println("Random Port Binding: " + randomPortBinding.hostPort());
        randomPort.add(randomPortBinding);
        portBindings.put("8080", randomPort);

        final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

// Create container with exposed ports
        final ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image(runtimeConfig.getProperties().get("name")).exposedPorts(ports)
                //                .cmd("sh", "-c", "while :; do sleep 1; done")
                .build();

        final ContainerCreation creation = docker.createContainer(containerConfig);

        final String id = creation.id();
        System.out.println(">>> ID: " + id);
// Inspect container
        final ContainerInfo info = docker.inspectContainer(id);
        System.out.println(">>> INFO: " + info);
        String shortId = id.substring(0, 12);
        Runtime runtime = new DockerRuntime(shortId, runtimeConfig, this);

        return runtime;

// Start container
//        docker.startContainer(id);
// Exec command inside running container with attached STDOUT and STDERR
//        final String[] command = {"bash", "-c", "ls"};
//        final String execId = docker.execCreate(id, command, DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
//        final LogStream output = docker.execStart(execId);
//        final String execOutput = output.readFully();
//// Kill container
//        docker.killContainer(id);
//
//// Remove container
//        docker.removeContainer(id);
    }

    public DockerClient getDocker() {
        return docker;
    }

    @Override
    public void destroy(String runtimeId) throws DockerException, InterruptedException {
        docker.killContainer(runtimeId);
        docker.removeContainer(runtimeId);
    }

}
