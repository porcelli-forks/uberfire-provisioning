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

package org.uberfire.provisioning.runtime.base;

import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.RuntimeEndpoint;
import org.uberfire.provisioning.runtime.RuntimeState;
import org.uberfire.provisioning.runtime.RuntimeInfo;
import org.uberfire.provisioning.runtime.providers.Provider;

public abstract class BaseRuntime implements Runtime {

    private String id;
    protected RuntimeConfiguration config;
    private RuntimeInfo info;
    private RuntimeState state;
    private RuntimeEndpoint endpoint;
    private Provider provider;

    public BaseRuntime( String id,
            RuntimeConfiguration config, Provider provider ) {
        this.id = id;
        this.config = config;
        this.provider = provider;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId( String id ) {
        this.id = id;
    }

    @Override
    public void setConfig( RuntimeConfiguration config ) {
        this.config = config;
    }

    @Override
    public RuntimeConfiguration getConfig() {
        return config;
    }

    @Override
    public RuntimeInfo getInfo() {
        return info;
    }

    @Override
    public void setInfo( RuntimeInfo info ) {
        this.info = info;
    }

    @Override
    public RuntimeState getState() {
        return state;
    }

    @Override
    public void setState( RuntimeState state ) {
        this.state = state;
    }

    @Override
    public RuntimeEndpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public void setEndpoint( RuntimeEndpoint endpoint ) {
        this.endpoint = endpoint;
    }

    @Override
    public Provider getProvider() {
        return provider;
    }

    @Override
    public void setProvider( Provider provider ) {
        this.provider = provider;
    }

}
