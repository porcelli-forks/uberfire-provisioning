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

import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.Stage;

import static java.lang.System.*;

/**
 * @author salaboy
 */
public class SimplePipelineInstance implements PipelineInstance {

    private final Pipeline pipeline;

    public SimplePipelineInstance( Pipeline pipeline ) {
        this.pipeline = pipeline;
    }

    @Override
    public void run( PipelineContext context ) {
        out.println( " >> Running Pipeline: " + pipeline.getName() );

        for ( Stage s : pipeline.getStages() ) {
            out.println( " Executing Stage: " + s.getName() );
            s.execute( context );
        }
    }
}
