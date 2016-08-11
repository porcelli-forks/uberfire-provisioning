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

package org.uberfire.provisioning.local.runtime.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.providers.ProviderService;

import static java.lang.System.*;
import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;

public class LocalRuntimeService implements RuntimeService {

    private ProviderService providerService;
    private org.uberfire.provisioning.runtime.Runtime runtime;

    private Process p;

    public LocalRuntimeService( ProviderService providerService,
                                Runtime runtime ) {
        this.providerService = providerService;
        this.runtime = runtime;
        if ( !( providerService instanceof LocalProviderService ) ) {
            throw new IllegalArgumentException( "Wrong provider! set: " + providerService.getClass() + " expected: LocalProvider" );
        }
    }

    @Override
    public void start() {
        try {
            p = java.lang.Runtime.getRuntime().exec( "java -jar " + runtime.getConfig().getProperties().get( "jar" ) );
            new Thread( new Runnable() {
                public void run() {
                    BufferedReader input = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
                    String line = null;

                    try {
                        while ( ( line = input.readLine() ) != null ) {
                            out.println( line );
                        }
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }
            } ).start();

        } catch ( IOException ex ) {
            getLogger( LocalRuntime.class.getName() ).log( SEVERE, null, ex );
        }

    }

    @Override
    public void stop() {
        Process destroyForcibly = p.destroyForcibly();
    }

    @Override
    public void restart() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public ProviderService getProviderService() {
        return providerService;
    }

    @Override
    public void setProviderService( ProviderService providerService ) {
        this.providerService = providerService;
    }

    @Override
    public Runtime getRuntime() {
        return runtime;
    }

    @Override
    public void setRuntime( Runtime runtime ) {
        this.runtime = runtime;
    }

}
