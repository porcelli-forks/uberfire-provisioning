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

package org.uberfire.provisioning.registry.local.tests;

import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.providers.ProviderService;

public class MyRuntimeService implements RuntimeService {
    
    @Override
    public void start() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void restart() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProviderService getProviderService() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setProviderService( ProviderService providerService ) {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Runtime getRuntime() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRuntime( Runtime runtime ) {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
