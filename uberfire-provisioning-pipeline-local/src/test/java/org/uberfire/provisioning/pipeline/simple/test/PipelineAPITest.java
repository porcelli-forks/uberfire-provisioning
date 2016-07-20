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
import org.junit.Test;
import org.uberfire.provisioning.pipeline.simple.provider.PipelineInstanceImpl;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.uberfire.provisioning.pipeline.PipelineDataContext;
import org.uberfire.provisioning.pipeline.PipelineTemplate;

/**
 *
 */
public class PipelineAPITest {

    public PipelineAPITest() {
    }

    @Test
    public void fluentAPITest() {

        Pipeline np = Pipeline.builder()
                .newPipeline( "my pipe" )
                .newStage( PrintOutStage.builder().withName( "my stage" )
                        .withMessage( "my message" ).outMessage( "result" ).build() )
                .newStage( PrintOutStage2.builder().withName( "my stage 2" ).withMessage( "${result}" ).outMessage( "result2" ).build() )
                .newStage( TestServiceStage.builder().withName( "my test service" ).withData( "${result2}" ).withRequiredService( MockService.class ).build() )
                .build();

        assertNotNull( np );

        List<Stage> stages = np.getStages();
        assertEquals( 3, stages.size() );

        assertEquals( "my stage", stages.get( 0 ).getName() );
        assertEquals( "my stage 2", stages.get( 1 ).getName() );
        assertEquals( "my test service", stages.get( 2 ).getName() );

        assertEquals( PrintOutStage.class, stages.get( 0 ).getClass() );
        assertEquals( PrintOutStage2.class, stages.get( 1 ).getClass() );
        assertEquals( TestServiceStage.class, stages.get( 2 ).getClass() );

        assertEquals( 1, np.getRequiredServices().size() );
        assertTrue( np.getRequiredServices().contains( MockService.class ) );

        PipelineInstanceImpl instance = new PipelineInstanceImpl( np );
        instance.registerService( MockService.class, new MockServiceImpl() );
        PipelineDataContext results = instance.execute();

        String result1 = ( String ) results.getData( "${result}" );
        String result2 = ( String ) results.getData( "${result2}" );

        assertEquals( "my message-v2", result1 );
        assertEquals( "my message-v2-v3", result2 );

        MockService service = instance.getService( MockService.class );

        assertEquals( "my message-v2-v3", ( ( MockServiceImpl ) service ).getTexts().get( 0 ) );
    }

    @Test
    public void templateTest() {
        PipelineTemplate template = PipelineTemplate.builder()
                .newTemplate( "My template" )
                .withPrefix( "template1" )
                .addStage( "Print Out Stage", PrintOutStage.class )
                .addStage( "Print Out Stage 2", PrintOutStage2.class )
                .addStage( "My Test Service", TestServiceStage.class )
                .addStage( "Print Out Stage 3", PrintOutStage.class )
                .build();

        // register template
        assertEquals( "My template", template.getName() );
        // Loop template to discover stages
        assertEquals( 4, template.getStages().size() );
        
        // Create Builder for stages based on template
        PrintOutStage.PrintOutStageBuilder printOutBuilder = PrintOutStage.builder().withName( template.getStages().get( 0 ).getName() );
        PrintOutStage2.PrintOutStage2Builder printOut2Builder = PrintOutStage2.builder().withName( template.getStages().get( 1 ).getName() );
        TestServiceStage.TestServiceStageBuilder testServiceBuilder = TestServiceStage.builder().withName( template.getStages().get( 2 ).getName() );
        PrintOutStage.PrintOutStageBuilder printOut3Builder = PrintOutStage.builder().withName( template.getStages().get( 3 ).getName() );

        // Build pipe based on template
        Pipeline pipe = Pipeline.builderFromTemplate( template )
                .withName( "My Pipeline" )
                .withStage( printOutBuilder.build() )
                .withStage( printOut2Builder.build() )
                .withStage( testServiceBuilder.build() )
                .withStage( printOut3Builder.build() )
                .build();

        assertEquals( "template1 - My Pipeline", pipe.getName() );

        assertEquals( 4, pipe.getStages().size() );
        assertEquals( "Print Out Stage", pipe.getStages().get( 0 ).getName() );
        assertEquals( "Print Out Stage 2", pipe.getStages().get( 1 ).getName() );
        assertEquals( "My Test Service", pipe.getStages().get( 2 ).getName() );
        assertEquals( "Print Out Stage 3", pipe.getStages().get( 3 ).getName() );
    }

}
