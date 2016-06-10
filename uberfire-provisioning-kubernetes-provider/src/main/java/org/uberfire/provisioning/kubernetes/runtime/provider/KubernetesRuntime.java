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

package org.uberfire.provisioning.kubernetes.runtime.provider;

import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.RuntimeInfo;
import org.uberfire.provisioning.runtime.RuntimeState;
import org.uberfire.provisioning.runtime.base.BaseRuntime;
import org.uberfire.provisioning.runtime.providers.Provider;

public class KubernetesRuntime extends BaseRuntime {

    public KubernetesRuntime( String id,
            RuntimeConfiguration config,
            Provider provider ) {
        super( id, config, provider );
        if ( !( provider instanceof KubernetesProvider ) ) {
            throw new IllegalArgumentException( "Wrong provider! set: " + provider.getClass() + " expected: DockerProvider" );
        }
    }

    @Override
    public void start() {
        System.out.println( "Doing nothing for now ..." );
    }

    @Override
    public void stop() {
        System.out.println( "Doing nothing for now ... " );
    }

    @Override
    public void restart() {
        System.out.println( "Doing nothing for now ..." );
    }

    @Override
    public RuntimeInfo getInfo() {
        return new KubernetesRuntimeInfo();
    }

    @Override
    public RuntimeState getState() {
        return new KubernetesRuntimeState();
    }

}
