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

import javax.xml.bind.annotation.XmlRootElement;

import org.uberfire.provisioning.pipeline.BaseStage;
import org.uberfire.provisioning.pipeline.BaseStageBuilder;

import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

@XmlRootElement
public class PrintOutStage extends BaseStage {

    private String message;
    private String messageHolder;

    public PrintOutStage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public String getMessageHolder() {
        return messageHolder;
    }

    public void setMessageHolder( String messageHolder ) {
        this.messageHolder = messageHolder;
    }

    @Override
    public void execute( PipelineInstance pipe, PipelineDataContext pipelineData ) {
        System.out.println( "My initial Message is: " + getMessage() );
        String newMessage = getMessage() + "-v2";
        System.out.println( "My transformed Message is: " + newMessage );
        pipelineData.setData( "${" + getMessageHolder() + "}", newMessage );
    }

    public static PrintOutStageBuilder builder() {
        return new PrintOutStageBuilder();

    }

    public static class PrintOutStageBuilder extends BaseStageBuilder<PrintOutStage>{

        private PrintOutStageBuilder() {
            stage = new PrintOutStage();
        }

        @Override
        public PrintOutStageBuilder withRequiredService( Class type ) {
            super.withRequiredService( type ); 
            return this;
        }

        @Override
        public PrintOutStageBuilder withName( String name ) {
            super.withName( name );
            return this;
        }

        public PrintOutStageBuilder withMessage( String message ) {
            stage.setMessage( message );
            return this;
        }

        public PrintOutStageBuilder outMessage( String messageHolder ) {
            stage.setMessageHolder( messageHolder );
            return this;
        }

       
    }
}
