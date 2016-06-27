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

package org.uberfire.provisioning.registry.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.registry.PipelineRegistry;

/**
 * @TODO: This is a not thread-safe implementation for local testing. A
 * more robust and distributed implementation should be provided for real
 * use cases. All the lookups mechanisms and structures needs to be improved for
 * performance.
 */
public class InMemoryPipelineRegistry implements PipelineRegistry {

    private final Map<String, Pipeline> pipelineById;

    public InMemoryPipelineRegistry() {
        pipelineById = new HashMap<>();

    }

    @Override
    public void registerPipeline( Pipeline pipeline ) {
        pipelineById.put( pipeline.getId(), pipeline );
    }

    @Override
    public Pipeline getPipelineById( String pipelineId ) {
        return pipelineById.get( pipelineId );
    }

    @Override
    public List<Pipeline> getAllPipelines() {
        return new ArrayList<Pipeline>( pipelineById.values() );
    }

}
