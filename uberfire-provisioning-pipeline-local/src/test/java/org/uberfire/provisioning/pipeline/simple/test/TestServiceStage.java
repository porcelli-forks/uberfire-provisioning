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

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;

import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

@XmlRootElement
public class TestServiceStage extends BaseStage {

    private String name;
    private List<Class> requiredServices;

    private String data;

    public TestServiceStage() {
    }

    public String getData() {
        return data;
    }

    public void setData( String data ) {
        this.data = data;
    }

    @Override
    public void execute( PipelineInstance pipe, PipelineDataContext pipelineData ) {
        if ( getData().startsWith( "${" ) ) {
            pipe.getService( MockService.class ).doSomething( ( String ) pipelineData.getData( getData() ) );
        } else {
            pipe.getService( MockService.class ).doSomething( getData() );
        }

    }

    public static TestServiceStageBuilder builder() {
        return new TestServiceStageBuilder();
    }

    public static class TestServiceStageBuilder extends BaseStageBuilder<TestServiceStage> {

        private TestServiceStageBuilder() {
            stage = new TestServiceStage();
        }

        @Override
        public TestServiceStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public TestServiceStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public TestServiceStageBuilder withData( String data ) {
            stage.setData( data );
            return this;
        }

    }
}
