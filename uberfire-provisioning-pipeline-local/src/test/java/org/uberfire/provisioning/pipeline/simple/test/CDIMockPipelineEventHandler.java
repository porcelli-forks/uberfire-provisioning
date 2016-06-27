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

package org.uberfire.provisioning.pipeline.simple.test;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.uberfire.provisioning.pipeline.events.AfterPipelineExecutionEvent;
import org.uberfire.provisioning.pipeline.events.AfterStageExecutionEvent;
import org.uberfire.provisioning.pipeline.events.BeforePipelineExecutionEvent;
import org.uberfire.provisioning.pipeline.events.BeforeStageExecutionEvent;
import org.uberfire.provisioning.pipeline.events.PipelineEvent;
import org.uberfire.provisioning.pipeline.events.PipelineEventHandler;

@ApplicationScoped
public class CDIMockPipelineEventHandler implements PipelineEventHandler {

    @Inject
    private Event<PipelineEvent> events;

    private List<PipelineEvent> firedEvents = new ArrayList<PipelineEvent>();

    @Override
    public void beforePipelineExecution( BeforePipelineExecutionEvent bpee ) {
        events.fire( bpee );
    }

    @Override
    public void afterPipelineExecution( AfterPipelineExecutionEvent apee ) {
        events.fire( apee );
    }

    @Override
    public void beforeStageExecution( BeforeStageExecutionEvent bsee ) {
        events.fire( bsee );
    }

    @Override
    public void afterStageExecution( AfterStageExecutionEvent asee ) {
        events.fire( asee );
    }

    public void observeFiredEvents( @Observes PipelineEvent event ) {
        firedEvents.add( event );
    }

    public List<PipelineEvent> getFiredEvents() {
        return firedEvents;
    }
}
