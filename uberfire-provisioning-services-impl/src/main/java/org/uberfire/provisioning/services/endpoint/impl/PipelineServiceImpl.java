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

package org.uberfire.provisioning.services.endpoint.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineInstance;
import org.uberfire.provisioning.services.endpoint.api.PipelineService;
import org.uberfire.provisioning.services.endpoint.api.RuntimeProvisioningService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

/**
 * @author salaboy
 */
@ApplicationScoped
public class PipelineServiceImpl implements PipelineService {

    @Context
    private SecurityContext context;

    private Map<String, Pipeline> pipelines = new HashMap<String, Pipeline>();

    @Inject
    private RuntimeProvisioningService provisioningService;

    public PipelineServiceImpl() {

    }

    @PostConstruct
    public void init() {
        System.out.println( "Post Construct Pipeline ServiceImpl here!" );
    }

    @Override
    public List<Pipeline> getAllPipelines() throws BusinessException {
        return new ArrayList<>( pipelines.values() );
    }

    @Override
    public String newPipeline( Pipeline pipeline ) throws BusinessException {
        String id = UUID.randomUUID().toString();
        pipeline.setId( id );
        pipelines.put( pipeline.getId(), pipeline );
        return pipeline.getId();
    }

    @Override
    public void runPipeline( String id,
                             PipelineContext context ) throws BusinessException {
        context.getServices().put( "provisioningService", provisioningService );
        new SimplePipelineInstance( pipelines.get( id ) ).run( context );
    }

}
