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

package org.uberfire.provisioning.services.pipeline;

import java.util.HashMap;
import java.util.Map;

import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.runtime.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.services.endpoint.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;

/**
 * @author salaboy
 */
public class ProvisionContainerStage implements Stage {

    private final String name = "provision stage";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute( PipelineContext context ) {
        RuntimeProvisioningService provisioningService = (RuntimeProvisioningService) context.getServices().get( "provisioningService" );
        BaseRuntimeConfiguration conf = new BaseRuntimeConfiguration();
        Map<String, String> props = new HashMap<String, String>();

        for ( String key : context.getData().keySet() ) {
            props.put( key, (String) context.getData().get( key ) );
        }
        conf.setProperties( props );
        try {
            provisioningService.newRuntime( conf );
        } catch ( BusinessException ex ) {
            getLogger( ProvisionContainerStage.class.getName() ).log( SEVERE, null, ex );
        }

    }

}
