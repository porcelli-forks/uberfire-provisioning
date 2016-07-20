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

package org.uberfire.provisioning.registry.local.tests;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.System.*;
import java.util.List;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineTemplate;
import org.uberfire.provisioning.registry.PipelineRegistry;
import org.uberfire.provisioning.registry.local.InMemoryPipelineRegistry;

@RunWith( Arquillian.class )
public class LocalPipelineRegistryTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = create( JavaArchive.class )
                .addClass( InMemoryPipelineRegistry.class )
                .addAsManifestResource( INSTANCE, "beans.xml" );
        out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    private PipelineRegistry registry;

    @Test
    public void pipelineAndTemplateTest() {
        assertNotNull( registry );
        List<PipelineTemplate> allTemplates = registry.getAllTemplates();
        assertEquals( 0, allTemplates.size() );

        PipelineTemplate template = PipelineTemplate.builder()
                .newTemplate( "My template" )
                .withPrefix( "template1" )
                .build();

        assertNotNull( template );
        registry.registerTemplate( template );

        allTemplates = registry.getAllTemplates();
        assertEquals( 1, allTemplates.size() );

        PipelineTemplate templateByName = registry.getTemplateByName( "My template" );
        assertNotNull( templateByName );

        List<Pipeline> allPipelines = registry.getAllPipelines();
        assertEquals( 0, allPipelines.size() );

        Pipeline pipe = Pipeline.builderFromTemplate( templateByName )
                        .withName( "My Pipe" ).build();
        assertNotNull( pipe );
        registry.registerPipeline( pipe );

        allPipelines = registry.getAllPipelines();
        assertEquals( 1, allPipelines.size() );

        Pipeline pipelineByName = registry.getPipelineByName( "template1 - My Pipe" );
        assertNotNull( pipelineByName );

    }

}
