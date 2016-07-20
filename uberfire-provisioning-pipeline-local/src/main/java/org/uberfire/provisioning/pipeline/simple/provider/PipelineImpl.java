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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.Stage;

public class PipelineImpl implements Pipeline {

    private String name;
    private List<Stage> stages;
    private Set<Class> requiredServices;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addStage( Stage stage ) {
        if ( stages == null ) {
            stages = new ArrayList<>();
        }
        stages.add( stage );
    }

    @Override
    public void addRequiredService( Class type ) {
        if ( requiredServices == null ) {
            requiredServices = new HashSet<>();
        }
        requiredServices.add( type );
    }

    @Override
    public List<Stage> getStages() {
        return stages;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public void setStages( List<Stage> stages ) {
        this.stages = stages;
    }

    @Override
    public Set<Class> getRequiredServices() {
        return requiredServices;
    }

}
