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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.maven.MavenBuild;
import org.uberfire.provisioning.build.maven.MavenProject;
import org.uberfire.provisioning.build.maven.stages.MavenBuildStage;
import org.uberfire.provisioning.build.maven.stages.MavenProjectConfigStage;
import org.uberfire.provisioning.exceptions.BuildException;

import org.uberfire.provisioning.exceptions.SourcingException;
import org.uberfire.provisioning.pipeline.simple.provider.PipelineInstanceImpl;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.registry.PipelineRegistry;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.registry.local.InMemoryBuildRegistry;
import org.uberfire.provisioning.registry.local.InMemoryPipelineRegistry;
import org.uberfire.provisioning.registry.local.InMemoryRuntimeRegistry;
import org.uberfire.provisioning.registry.local.InMemorySourceRegistry;
import org.uberfire.provisioning.runtime.RuntimeEndpoint;

import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderType;

import org.uberfire.provisioning.source.git.GitSource;
import org.uberfire.provisioning.source.git.stages.GitSourceStage;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderType;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfBuilder;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.stages.WildflyProvisionRuntimeStage;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.PipelineInstance;
import org.uberfire.provisioning.pipeline.PipelineDataContext;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.providers.ProviderService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProvider;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfBuilder;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfiguration;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderService;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderType;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntimeService;
import org.uberfire.provisioning.openshift.runtime.provider.stages.OpenshiftProvisionRuntimeStage;

/*
 *  mvn -Dtest=PipelineIntegrationAPITest -Dmaven.multiModuleProjectDirectory=$M2_HOME test
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
                .addClass( OpenshiftProviderType.class )
                .addClass( Wildfly10ProviderType.class )
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
    public void openshiftPipelinedAPIsTest() throws SourcingException, BuildException {

        List<ProviderType> allProviderTypes = runtimeRegistry.getAllProviderTypes();

        assertEquals( 2, allProviderTypes.size() );

        ProviderType openshiftProviderType = runtimeRegistry.getProviderTypeByName( "openshift" );
        OpenshiftProviderConfiguration openshiftProviderConfig = OpenshiftProviderConfBuilder.newConfig( "openshift origin" )
                .setMasterUrl( "https://10.2.2.2:8443" ).setUsername( "admin" ).setPassword( "admin" )
                .get();

        OpenshiftProvider openshiftProvider = new OpenshiftProvider( openshiftProviderConfig, openshiftProviderType );
        runtimeRegistry.registerProvider( openshiftProvider );

        List<Provider> allProviders = runtimeRegistry.getAllProviders();

        assertEquals( 1, allProviders.size() );
        String serviceName = "testservice";

        Pipeline np = Pipeline.builder()
                .newPipeline( "my pipe" )
                .newStage( GitSourceStage.builder().withName( "Git Clone Stage" )
                        .withURI( "git://livespark-playground" )
                        .withOrigin( "https://github.com/pefernan/livespark-playground" )
                        .withRepository( "livespark-playground" )
                        .withPath( tempPath )
                        .outSource( "source" )
                        .build() )
                .newStage( MavenProjectConfigStage.builder().withName( "Maven Project Config Stage" )
                        .withProjectName( "users-new" )
                        .withExpectedBinary( "users-new-swarm.jar" )
                        .inSource( "${source}" )
                        .outProject( "project" )
                        .outWarPath( "warPath" )
                        .build() )
                .newStage( MavenBuildStage.builder().withName( "Build Stage" )
                        .inProject( "${project}" )
                        .build() )
                .newStage( OpenshiftProvisionRuntimeStage.builder().withName( "Provision To Openshift Stage" )
                        .withProviderName( "openshift origin" )
                        .withServiceName( serviceName )
                        .withLabel( "uberfire" )
                        .withNamespace( "default" )
                        .withInternalPort( "8080" )
                        .withImage( "salaboy/users-new" )
                        .outRuntimeId( "runtimeId" )
                        .build() )
                .build();

        pipelineRegistry.registerPipeline( np );

        List<Pipeline> allPipelines = pipelineRegistry.getAllPipelines();
        assertEquals( 1, allPipelines.size() );

        assertEquals( 3, np.getRequiredServices().size() );

        assertTrue( np.getRequiredServices().contains( SourceRegistry.class ) );
        assertTrue( np.getRequiredServices().contains( Build.class ) );
        assertTrue( np.getRequiredServices().contains( RuntimeRegistry.class ) );

        PipelineInstance newPipelineInstance = new PipelineInstanceImpl( np );
        newPipelineInstance.registerEventHandler( eventHandler );
        newPipelineInstance.registerService( SourceRegistry.class, sourceRegistry );
        newPipelineInstance.registerService( Build.class, buildService );
        newPipelineInstance.registerService( RuntimeRegistry.class, runtimeRegistry );

        PipelineDataContext results = newPipelineInstance.execute();

        String runtimeId = ( String ) results.getData( "${runtimeId}" );

        assertEquals( 10, eventHandler.getFiredEvents().size() );

        assertEquals( serviceName, runtimeId );

        Runtime runtimeById = runtimeRegistry.getRuntimeById( runtimeId );
        assertNotNull( runtimeById );

        ProviderService providerService = new OpenshiftProviderService( openshiftProvider );

        RuntimeService runtimeService = new OpenshiftRuntimeService( providerService, runtimeById );
        runtimeService.refresh();
        
        RuntimeEndpoint endpoint = runtimeById.getEndpoint();
        assertNotNull( endpoint );
        assertEquals( serviceName + ".apps.10.2.2.2.xip.io", endpoint.getHost() );
    }

    /*
     * Ignoring this test because it requires Wildfly Running on localhost
     * mvn -Dtest=PipelineIntegrationAPITest -Dmaven.multiModuleProjectDirectory=$M2_HOME test
     */
    @Test
    @Ignore
    public void wildflyPipelinedAPIsTest() throws SourcingException, BuildException {

        List<ProviderType> allProviderTypes = runtimeRegistry.getAllProviderTypes();

        assertEquals( 2, allProviderTypes.size() );

        ProviderType wildflyProviderType = runtimeRegistry.getProviderTypeByName( "wildfly" );
        WildflyProviderConfiguration wildflyProviderConfig = WildflyProviderConfBuilder.newConfig( "wildfly @ localhost" )
                .setHost( "localhost" )
                .setManagementPort( "9990" )
                .setPort( "8080" )
                .setUser( "salaboy" ).setPassword( "salaboy123$" )
                .get();

        Wildfly10Provider wildflyProvider = new Wildfly10Provider( wildflyProviderConfig, wildflyProviderType );
        runtimeRegistry.registerProvider( wildflyProvider );

        List<Provider> allProviders = runtimeRegistry.getAllProviders();

        assertEquals( 1, allProviders.size() );
        String appContext = "/";
        Pipeline np = Pipeline.builder()
                .newPipeline( "my pipe" )
                .newStage( GitSourceStage.builder().withName( "Git Clone Stage" )
                        .withURI( "git://livespark-playground" )
                        .withOrigin( "https://github.com/pefernan/livespark-playground" )
                        .withRepository( "livespark-playground" )
                        .withPath( tempPath )
                        .outSource( "source" )
                        .build() )
                .newStage( MavenProjectConfigStage.builder().withName( "Maven Project Config Stage" )
                        .withProjectName( "users-new" )
                        .withExpectedBinary( "users-new.war" )
                        .inSource( "${source}" )
                        .outProject( "project" )
                        .outWarPath( "warPath" )
                        .build() )
                .newStage( MavenBuildStage.builder().withName( "Build Stage" )
                        .inProject( "${project}" )
                        .build() )
                .newStage( WildflyProvisionRuntimeStage.builder().withName( "Provision To Wildfly Stage" )
                        .withProviderName( "wildfly @ localhost" )
                        .withAppContext( appContext )
                        .inWarPath( "${warPath}" )
                        .outRuntimeId( "runtimeId" )
                        .build() )
                .build();
        pipelineRegistry.registerPipeline( np );

        List<Pipeline> allPipelines = pipelineRegistry.getAllPipelines();
        assertEquals( 1, allPipelines.size() );

        assertEquals( 3, np.getRequiredServices().size() );

        assertTrue( np.getRequiredServices().contains( SourceRegistry.class ) );
        assertTrue( np.getRequiredServices().contains( Build.class ) );
        assertTrue( np.getRequiredServices().contains( RuntimeRegistry.class ) );

        PipelineInstance newPipelineInstance = new PipelineInstanceImpl( np );
        newPipelineInstance.registerEventHandler( eventHandler );
        newPipelineInstance.registerService( SourceRegistry.class, sourceRegistry );
        newPipelineInstance.registerService( Build.class, buildService );
        newPipelineInstance.registerService( RuntimeRegistry.class, runtimeRegistry );

        PipelineDataContext results = newPipelineInstance.execute();

        String runtimeId = ( String ) results.getData( "${runtimeId}" );

        assertEquals( 10, eventHandler.getFiredEvents().size() );

        Runtime runtimeById = runtimeRegistry.getRuntimeById( runtimeId );

        assertNotNull( runtimeById );

        RuntimeEndpoint endpoint = runtimeById.getEndpoint();
        assertNotNull( endpoint );

        System.out.println( ">> Endpoint : " + endpoint.getHost() + ":" + endpoint.getPort() + "/" + endpoint.getContext() );
        assertEquals( "localhost", endpoint.getHost() );
        assertEquals( 8080, endpoint.getPort() );
        assertEquals( appContext, endpoint.getContext() );
    }
}
