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

import org.uberfire.provisioning.pipeline.StageDefinitionImpl;
import java.util.List;
import org.uberfire.provisioning.pipeline.PipelineTemplate;
import org.uberfire.provisioning.pipeline.StageDefinition;

public class PipelineTemplateImpl implements PipelineTemplate {

    private String name;
    private String prefix;

    private List<StageDefinition> stages;

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public void addStage( String stageName, Class type ) {
        stages.add( new StageDefinitionImpl( name, type ) );
    }

    @Override
    public List<StageDefinition> getStages() {
        return stages;
    }

    @Override
    public void setStages( List<StageDefinition> stages ) {
        this.stages = stages;
    }

    @Override
    public String getName() {
        return name;
    }

}
