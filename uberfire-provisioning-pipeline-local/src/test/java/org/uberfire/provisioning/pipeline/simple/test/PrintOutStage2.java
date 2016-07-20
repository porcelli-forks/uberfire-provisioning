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
public class PrintOutStage2 extends BaseStage {

    private String message;
    private String messageHolder;

    public PrintOutStage2() {
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
        String message;
        if ( getMessage().startsWith( "${" ) ) {
            message = ( String ) pipelineData.getData( getMessage() );
            System.out.println( " Message resolved from PipelineData: " + message );
        } else {
            message = getMessage();
            System.out.println( " Message Straight from Conf: " + message );
        }

        if ( getMessageHolder() != null ) {
            pipelineData.setData( "${" + getMessageHolder() + "}", message + "-v3" );
        }

    }

    public static PrintOutStage2Builder builder() {
        return new PrintOutStage2Builder();

    }

    public static class PrintOutStage2Builder extends BaseStageBuilder<PrintOutStage2> {

        private PrintOutStage2Builder() {
            stage = new PrintOutStage2();
        }

        @Override
        public PrintOutStage2Builder withRequiredService( Class type ) {
            super.withRequiredService( type );
            return this;
        }

        @Override
        public PrintOutStage2Builder withName( String name ) {
            super.withName( name );
            return this;
        }

        public PrintOutStage2Builder withMessage( String message ) {
            stage.setMessage( message );
            return this;
        }

        public PrintOutStage2Builder outMessage( String messageHolder ) {
            stage.setMessageHolder( messageHolder );
            return this;
        }

    }

}
