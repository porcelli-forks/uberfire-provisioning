/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.provisioning.runtime.providers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.*;

/**
 * @author salaboy
 *         <p>
 *         A provider represent a running entity that allows us to provision new
 *         runtimes. Such as: Docker, Kubernetes, Application Servers (Wildfly, Tomcat,
 *         etc)
 */
@JsonTypeInfo(use = CLASS, include = WRAPPER_OBJECT)
public interface Provider {

    String getName();

    ProviderConfiguration getConfig();

    ProviderType getProviderType();

    Runtime create( RuntimeConfiguration config ) throws ProvisioningException;

    void destroy( String runtimeId ) throws ProvisioningException;

}
