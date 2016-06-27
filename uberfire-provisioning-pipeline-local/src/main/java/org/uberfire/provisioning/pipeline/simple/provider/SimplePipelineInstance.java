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

package org.uberfire.provisioning.pipeline.simple.provider;

import java.util.ArrayList;
import java.util.List;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.Stage;
import org.uberfire.provisioning.pipeline.events.AfterPipelineExecutionEvent;
import org.uberfire.provisioning.pipeline.events.AfterStageExecutionEvent;
import org.uberfire.provisioning.pipeline.events.BeforePipelineExecutionEvent;
import org.uberfire.provisioning.pipeline.events.BeforeStageExecutionEvent;

import org.uberfire.provisioning.pipeline.events.PipelineEventHandler;

public class SimplePipelineInstance implements PipelineInstance {

    private final Pipeline pipeline;

    private List<PipelineEventHandler> handlers;

    public SimplePipelineInstance( Pipeline pipeline ) {
        this.pipeline = pipeline;
    }

    @Override
    public void run( PipelineContext context ) {
        if ( handlers == null ) {
            handlers = new ArrayList<PipelineEventHandler>();
            handlers.add( new DefaultPipelineEventHandler() );
        }
        for ( PipelineEventHandler h : handlers ) {
            h.beforePipelineExecution( new BeforePipelineExecutionEvent( pipeline ) );
        }

        for ( Stage s : pipeline.getStages() ) {
            for ( PipelineEventHandler h : handlers ) {
                h.beforeStageExecution( new BeforeStageExecutionEvent( s ) );
            }

            s.execute( context );

            for ( PipelineEventHandler h : handlers ) {
                h.afterStageExecution( new AfterStageExecutionEvent( s ) );
            }

        }
        for ( PipelineEventHandler h : handlers ) {
            h.afterPipelineExecution( new AfterPipelineExecutionEvent( pipeline ) );
        }

    }

    @Override
    public void registerEventHandler( PipelineEventHandler handler ) {
        if ( handlers == null ) {
            handlers = new ArrayList<PipelineEventHandler>();
        }
        handlers.add( handler );
    }
}
