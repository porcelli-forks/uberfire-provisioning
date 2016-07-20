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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@JsonTypeInfo( use = Id.CLASS, include = As.WRAPPER_OBJECT )
public interface Pipeline {

    String getName();

    void setName( String id );

    void addStage( Stage stage );

    void setStages( List<Stage> stages );

    void addRequiredService( Class type );

    Set<Class> getRequiredServices();

    List<Stage> getStages();

    public static PipelineBuilder builder() {
        return new PipelineBuilder();
    }

    public static PipelineBuilderFromTemplate builderFromTemplate( PipelineTemplate template ) {
        return new PipelineBuilderFromTemplate( template );
    }

    public static class PipelineBuilder {

        private String name;
        private Set<Class> requiredServices = new HashSet<>();
        private List<Stage> stages = new ArrayList<>();
        public static final String TYPE = "org.uberfire.provisioning.pipeline.simple.provider.PipelineImpl";

        public PipelineBuilder newPipeline( String name ) {
            this.name = name;
            return this;
        }

        public PipelineBuilder addRequiredService( Class type ) {
            requiredServices.add( type );
            return this;
        }

        public PipelineBuilder newStage( Stage stage ) {
            stages.add( stage );
            return this;
        }

        public Pipeline build() {
            Pipeline p;
            try {
                p = ( Pipeline ) Class.forName( TYPE ).newInstance();
            } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException ex ) {
                Logger.getLogger( Pipeline.class.getName() ).log( Level.SEVERE, null, ex );
                return null;
            }
            p.setName( name );
            if ( stages != null ) {
                p.setStages( stages );
                for ( Stage s : stages ) {
                    for ( Class type : s.getRequiredServices() ) {
                        p.addRequiredService( type );
                    }
                }
            }
            return p;
        }
    }

    public static class PipelineBuilderFromTemplate {

        private PipelineTemplate template;
        private String name;
        private List<Stage> stageConf = new ArrayList<>();
        public static final String TYPE = "org.uberfire.provisioning.pipeline.simple.provider.PipelineImpl";

        public PipelineBuilderFromTemplate( PipelineTemplate template ) {
            this.template = template;
        }

        public PipelineBuilderFromTemplate withName( String name ) {
            this.name = name;
            return this;
        }

        public PipelineBuilderFromTemplate withStage( Stage stage ) {
            stageConf.add( stage );
            return this;
        }

        public Pipeline build() {
            Pipeline p;
            try {
                p = ( Pipeline ) Class.forName( TYPE ).newInstance();
            } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException ex ) {
                Logger.getLogger( Pipeline.class.getName() ).log( Level.SEVERE, null, ex );
                return null;
            }
            p.setName( template.getPrefix() + " - " + name );
            if ( template.getStages() != null ) {
                for ( int i = 0; i < template.getStages().size(); i++ ) {
                    Class clazz = template.getStages().get( i ).getType();
                    if ( clazz.isInstance( stageConf.get( i ) ) ) {
                        p.addStage( stageConf.get( i ) );
                    }
                }
            }
            return p;
        }
    }

}
