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
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.simple.provider.PrintOutStage;
import org.uberfire.provisioning.pipeline.simple.provider.PrintOutStage2;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipeline;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineContext;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineInstance;

@RunWith( Arquillian.class )
public class CDIPipelineEventsAPITest {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create( JavaArchive.class )
                .addClass( CDIMockPipelineEventHandler.class )
                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" );
        System.out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    private CDIMockPipelineEventHandler eventHandler;

    @Test
    public void helloCDIPipeline() {

        Pipeline p = new SimplePipeline( "simple pipe" );
        p.addStage( new PrintOutStage( "Simple Print" ) );
        p.addStage( new PrintOutStage2( "Second Print" ) );

        PipelineInstance simplePipelineInstance = new SimplePipelineInstance( p );
        simplePipelineInstance.registerEventHandler( eventHandler );

        PipelineContext simplePipelineContext = new SimplePipelineContext();
        String message = "hi there!";
        simplePipelineContext.getData().put( "message", message );

        List<String> messagesList = new ArrayList<String>();
        simplePipelineContext.getServices().put( "messages", messagesList );

        simplePipelineInstance.run( simplePipelineContext );

        assertEquals( 2, messagesList.size() );

        assertEquals( ">>> Message: " + message, messagesList.get( 0 ) );
        assertEquals( ">>> Message Version 2: " + message, messagesList.get( 1 ) );

        assertEquals( 6, eventHandler.getFiredEvents().size() );

    }

}
