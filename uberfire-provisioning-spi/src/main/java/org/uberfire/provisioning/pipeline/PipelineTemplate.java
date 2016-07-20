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

package org.uberfire.provisioning.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface PipelineTemplate {

    void setName( String name );

    void setPrefix( String prefix );

    String getPrefix();

    void addStage( String stageName, Class type );

    List<StageDefinition> getStages();

    void setStages( List<StageDefinition> stages );

    String getName();

    public static PipelineTemplateBuilder builder() {
        return new PipelineTemplateBuilder();
    }

    public static class PipelineTemplateBuilder {

        private String name;
        private String prefix;
        private List<StageDefinition> stages;
        public static final String TYPE = "org.uberfire.provisioning.pipeline.simple.provider.PipelineTemplateImpl";

        public PipelineTemplateBuilder newTemplate( String name ) {
            this.name = name;
            return this;
        }

        public PipelineTemplateBuilder withPrefix( String prefix ) {
            this.prefix = prefix;
            return this;
        }

        public PipelineTemplateBuilder addStage( String name, Class type ) {
            if ( stages == null ) {
                stages = new ArrayList<StageDefinition>();
            }
            stages.add( new StageDefinitionImpl(name, type) );

            return this;
        }

        public PipelineTemplate build() {
            PipelineTemplate pt;
            try {
                pt = ( PipelineTemplate ) Class.forName( TYPE ).newInstance();
            } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException ex ) {
                Logger.getLogger( Pipeline.class.getName() ).log( Level.SEVERE, null, ex );
                return null;
            }
            pt.setName( name );
            pt.setPrefix( prefix );
            pt.setStages( stages );

            return pt;
        }
    }
}
