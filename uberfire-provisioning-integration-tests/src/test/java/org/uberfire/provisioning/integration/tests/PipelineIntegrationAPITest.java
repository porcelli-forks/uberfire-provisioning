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

package org.uberfire.provisioning.integration.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.maven.MavenBuild;
import org.uberfire.provisioning.build.maven.stages.MavenBuildStage;
import org.uberfire.provisioning.build.maven.MavenProject;
import org.uberfire.provisioning.build.maven.MavenProjectConfigurationStage;
import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.exceptions.SourcingException;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProvider;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProviderConfBuilder;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProviderConfiguration;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProviderType;
import org.uberfire.provisioning.kubernetes.runtime.provider.stages.KubernetesProvisionRuntimeStage;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineContext;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipeline;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineContext;
import org.uberfire.provisioning.pipeline.simple.provider.SimplePipelineInstance;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.registry.PipelineRegistry;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.registry.local.InMemoryBuildRegistry;
import org.uberfire.provisioning.registry.local.InMemoryPipelineRegistry;
import org.uberfire.provisioning.registry.local.InMemoryRuntimeRegistry;
import org.uberfire.provisioning.registry.local.InMemorySourceRegistry;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.source.git.GitSource;
import org.uberfire.provisioning.source.git.stages.GitSourceStage;

/*
 * mvn -Dtest=PipelineIntegrationAPITest test
 */
@RunWith( Arquillian.class )
public class PipelineIntegrationAPITest {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create( JavaArchive.class )
                .addClass( CDIMockPipelineEventHandler.class )
                .addClass( GitSource.class )
                .addClass( MavenBuild.class )
                .addClass( MavenProject.class )
                .addClass( InMemorySourceRegistry.class )
                .addClass( InMemoryBuildRegistry.class )
                .addClass( InMemoryPipelineRegistry.class )
                .addClass( InMemoryRuntimeRegistry.class )
                .addClass( KubernetesProviderType.class )
                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" );
        System.out.println( jar.toString( true ) );
        return jar;
    }

    private File tempPath;

    @Inject
    private PipelineRegistry pipelineRegistry;

    @Inject
    private CDIMockPipelineEventHandler eventHandler;

    @Inject
    private SourceRegistry sourceRegistry;

    @Inject
    private Build buildService;

    @Inject
    private BuildRegistry buildRegistry;

    @Inject
    private RuntimeRegistry runtimeRegistry;

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    @Before
    public void setUp() {
        try {
            tempPath = Files.createTempDirectory( "xxx" ).toFile();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        for ( ProviderType pt : providerTypes ) {
            runtimeRegistry.registerProviderType( pt );
        }
    }

    @After
    public void tearDown() {
        FileUtils.deleteQuietly( tempPath );
    }

    /*
     * Ignoring this test because it requires Openshift Origin + M2_HOME set 
     * mvn -Dtest=PipelineIntegrationAPITest -Dmaven.multiModuleProjectDirectory=$M2_HOME test
    */
    @Test
    @Ignore
    public void helloPipelinedAPIs() throws SourcingException, BuildException {

        List<ProviderType> allProviderTypes = runtimeRegistry.getAllProviderTypes();

        assertEquals( 1, allProviderTypes.size() );

        ProviderType kubernetesProviderType = runtimeRegistry.getProviderTypeByName( "kubernetes" );
        KubernetesProviderConfiguration kubeProviderConfig = KubernetesProviderConfBuilder.newConfig( "kubernetes @ openshift origin" )
                .setMasterUrl( "https://10.2.2.2:8443" ).setUsername( "admin").setPassword( "admin")
                .get();

        KubernetesProvider kubernetesProvider = new KubernetesProvider( kubeProviderConfig, kubernetesProviderType );
        runtimeRegistry.registerProvider( kubernetesProvider );

        List<Provider> allProviders = runtimeRegistry.getAllProviders();

        assertEquals( 1, allProviders.size() );

        Pipeline p = new SimplePipeline( "simple pipe" );

        p.addStage( new GitSourceStage() );

        p.addStage( new MavenProjectConfigurationStage() );

        p.addStage( new MavenBuildStage() );

        p.addStage( new KubernetesProvisionRuntimeStage() );

        pipelineRegistry.registerPipeline( p );

        List<Pipeline> allPipelines = pipelineRegistry.getAllPipelines();
        assertEquals( 1, allPipelines.size() );

        PipelineInstance simplePipelineInstance = new SimplePipelineInstance( p );
        simplePipelineInstance.registerEventHandler( eventHandler );

        PipelineContext simplePipelineContext = new SimplePipelineContext();

        // GitSourceStage
        simplePipelineContext.getData().put( "uri", "git://livespark-playground" );
        simplePipelineContext.getData().put( "origin", "https://github.com/pefernan/livespark-playground" );
        simplePipelineContext.getData().put( "repository", "livespark-playground" );
        simplePipelineContext.getData().put( "branch", "master" );
        simplePipelineContext.getData().put( "path", tempPath );

        //MavenProjectConfigurationStage
        simplePipelineContext.getData().put( "projectName", "users-new" );
        simplePipelineContext.getData().put( "expectedBinary", "users-new-swarm.jar" );

        //MavenBuildStage
        //Provision Runtime to Kubernetes Stage
        simplePipelineContext.getData().put( "providerName", "kubernetes @ openshift origin" );
        simplePipelineContext.getData().put( "namespace", "default" );
        simplePipelineContext.getData().put( "label", "uberfire" );
        simplePipelineContext.getData().put( "image", "kitematic/hello-world-nginx" );
        simplePipelineContext.getData().put( "serviceName", "testservice" );
        simplePipelineContext.getData().put( "internalPort", "8080" );

        simplePipelineContext.getServices().put( "sourceRegistry", sourceRegistry );
        simplePipelineContext.getServices().put( "buildRegistry", buildRegistry );
        simplePipelineContext.getServices().put( "runtimeRegistry", runtimeRegistry );
        simplePipelineContext.getServices().put( "buildService", buildService );

        simplePipelineInstance.run( simplePipelineContext );

        assertEquals( 10, eventHandler.getFiredEvents().size() );
        
        

    }

}
