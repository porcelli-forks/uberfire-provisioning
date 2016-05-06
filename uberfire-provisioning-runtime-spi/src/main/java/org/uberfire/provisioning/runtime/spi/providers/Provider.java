/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.runtime.spi.providers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.exception.ProvisioningException;

/**
 *
 * @author salaboy
 *
 * A provider represent a running entity that allows us to provision new
 * runtimes. Such as: Docker, Kubernetes, Application Servers (Wildfly, Tomcat,
 * etc)
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface Provider {

    public String getName();

    public ProviderConfiguration getConfig();

    public ProviderType getProviderType();

    public Runtime create(RuntimeConfiguration config) throws ProvisioningException;

    public void destroy(String runtimeId) throws ProvisioningException;

}
