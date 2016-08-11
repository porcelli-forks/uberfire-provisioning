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

package org.uberfire.provisioning.services.rest;

import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineDataContext;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.events.PipelineEventHandler;
import org.uberfire.provisioning.pipeline.simple.provider.PipelineInstanceImpl;
import org.uberfire.provisioning.registry.PipelineRegistry;
import org.uberfire.provisioning.services.api.PipelineService;
import org.uberfire.provisioning.services.exceptions.BusinessException;

@ApplicationScoped
public class RestPipelineServiceImpl implements PipelineService {

    @Inject
    private PipelineRegistry pipelineRegistry;

    @Inject
    @Any
    private Instance<PipelineEventHandler> eventHandlers;

    @PostConstruct
    public void init() {

    }

    @Override
    public List<Pipeline> getAllPipelines() throws BusinessException {
        return pipelineRegistry.getAllPipelines();
    }

    @Override
    public String newPipeline( Pipeline pipeline ) throws BusinessException {
        String id = UUID.randomUUID().toString();
        pipeline.setName( id );
        pipelineRegistry.registerPipeline( pipeline );
        return pipeline.getName();
    }

    @Override
    public void runPipeline( final String name ) throws BusinessException {

        PipelineInstance newPipelineInstance = new PipelineInstanceImpl( pipelineRegistry.getPipelineByName( name ) );

        for ( PipelineEventHandler peh : eventHandlers ) {
            newPipelineInstance.registerEventHandler( peh );
        }
        PipelineDataContext results = newPipelineInstance.execute();

    }

}
