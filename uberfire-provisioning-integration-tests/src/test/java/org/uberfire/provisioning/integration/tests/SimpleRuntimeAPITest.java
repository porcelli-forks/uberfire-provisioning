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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.docker.runtime.provider.DockerProvider;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfBuilder;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfiguration;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderService;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderType;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntimeConfBuilder;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.local.runtime.provider.LocalProvider;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderConfBuilder;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderConfiguration;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderService;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderType;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntimeConfBuilder;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntimeService;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProvider;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfBuilder;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfiguration;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderService;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderType;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntimeConfBuilder;
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.registry.local.InMemoryRuntimeRegistry;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfBuilder;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeConfBuilder;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderService;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderType;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class SimpleRuntimeAPITest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create( JavaArchive.class )
                .addClass( ProviderType.class )
                .addClass( LocalProviderType.class )
                .addClass( Wildfly10ProviderType.class )
                .addClass( DockerProviderType.class )
                .addClass( OpenshiftProviderType.class )
                .addClass( InMemoryRuntimeRegistry.class )
                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" );
//        System.out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    private RuntimeRegistry registry;

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        for ( ProviderType pt : providerTypes ) {
            registry.registerProviderType( pt );
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void helloAPIs() {

        List<ProviderType> allProviderTypes = registry.getAllProviderTypes();

        Assert.assertEquals( 4, allProviderTypes.size() );

        ProviderType localProviderType = registry.getProviderTypeByName( "local" );
        LocalProviderConfiguration localProviderConfig = LocalProviderConfBuilder.newConfig( "local" ).get();

        LocalProvider localProvider = new LocalProvider( localProviderConfig, localProviderType );
        LocalProviderService localProviderService = new LocalProviderService( localProvider );
        registry.registerProvider( localProvider );

        ProviderType wildflyProviderType = registry.getProviderTypeByName( "wildfly" );

        WildflyProviderConfiguration wildflyProviderConfig = WildflyProviderConfBuilder.newConfig( "wildfly @ 9990" )
                .setHost( "localhost" )
                .setManagementPort( "9990" )
                .setUser( "salaboy" )
                .setPassword( "salaboy123$" ).get();

        Wildfly10Provider wildflyProvider = new Wildfly10Provider( wildflyProviderConfig, wildflyProviderType );
        Wildfly10ProviderService wildflyProviderService = new Wildfly10ProviderService( wildflyProvider );

        Assert.assertNotNull( wildflyProviderService.getWildfly() );
        registry.registerProvider( wildflyProvider );

        ProviderType openshiftProviderType = registry.getProviderTypeByName( "openshift" );
        OpenshiftProviderConfiguration openshiftProviderConfig = OpenshiftProviderConfBuilder.newConfig( "openshift origin" )
                .get();

        OpenshiftProvider openshiftProvider = new OpenshiftProvider( openshiftProviderConfig, openshiftProviderType );
        OpenshiftProviderService openshiftProviderService = new OpenshiftProviderService( openshiftProvider );
        registry.registerProvider( openshiftProvider );

        ProviderType dockerProviderType = registry.getProviderTypeByName( "docker" );
        DockerProviderConfiguration dockerProviderConfig = DockerProviderConfBuilder.newConfig( "docker local deamon" )
                .get();

        DockerProvider dockerProvider = new DockerProvider( dockerProviderConfig, dockerProviderType );
        DockerProviderService dockerProviderService = new DockerProviderService( dockerProvider );
        registry.registerProvider( dockerProvider );

        List<Provider> allProviders = registry.getAllProviders();

        Assert.assertEquals( 4, allProviders.size() );

        RuntimeConfiguration localRuntimeConfig = LocalRuntimeConfBuilder.newConfig()
                .setJar( "../extras/sample-war/target/sample-war-1.0-SNAPSHOT-swarm.jar" )
                .get();

        Runtime newLocalRuntime;
        try {
            newLocalRuntime = localProviderService.create( localRuntimeConfig );
            Assert.assertNotNull( newLocalRuntime );
            Assert.assertNotNull( newLocalRuntime.getId() );
            registry.registerRuntime( newLocalRuntime );
            LocalRuntimeService localRuntimeService = new LocalRuntimeService( localProviderService, newLocalRuntime );

            localRuntimeService.start();
            localRuntimeService.stop();
        } catch ( Exception ex ) {
            ex.printStackTrace();
            Logger.getLogger( SimpleRuntimeAPITest.class.getName() ).log( Level.SEVERE, null, ex );
            assertTrue( ex instanceof ProvisioningException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

        RuntimeConfiguration wildflyRuntimeConfig = WildflyRuntimeConfBuilder.newConfig()
                .setWarPath( "../extras/sample-war/target/sample-war-1.0-SNAPSHOT.war" )
                .get();

        Runtime newWildflyRuntime;
        try {
            newWildflyRuntime = wildflyProviderService.create( wildflyRuntimeConfig );
            assertNotNull( newWildflyRuntime );
            assertNotNull( newWildflyRuntime.getId() );
            registry.registerRuntime( newWildflyRuntime );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            Logger.getLogger( SimpleRuntimeAPITest.class.getName() ).log( Level.SEVERE, null, ex );
            assertTrue( ex instanceof ProvisioningException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

        RuntimeConfiguration openshiftRuntimeConfig = OpenshiftRuntimeConfBuilder.newConfig()
                .setNamespace( "default" )
                .setReplicationController( "test" )
                .setLabel( "uberfire" )
                .setServiceName( "test" )
                .setInternalPort( "8080" )
                .setImage( "kitematic/hello-world-nginx" )
                .get();

        Runtime newOpenshiftRuntime;
        try {
            newOpenshiftRuntime = openshiftProviderService.create( openshiftRuntimeConfig );
            assertNotNull( newOpenshiftRuntime );
            assertNotNull( newOpenshiftRuntime.getId() );
            registry.registerRuntime( newOpenshiftRuntime );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            Logger.getLogger( SimpleRuntimeAPITest.class.getName() ).log( Level.SEVERE, null, ex );
            assertTrue( ex instanceof ProvisioningException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

        RuntimeConfiguration dockerRuntimeConfig = DockerRuntimeConfBuilder.newConfig()
                .setPull( true )
                .setImage( "kitematic/hello-world-nginx" )
                .get();

        Runtime newDockerRuntime;
        try {
            newDockerRuntime = dockerProviderService.create( dockerRuntimeConfig );
            assertNotNull( newDockerRuntime );
            assertNotNull( newDockerRuntime.getId() );
            registry.registerRuntime( newDockerRuntime );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            Logger.getLogger( SimpleRuntimeAPITest.class.getName() ).log( Level.SEVERE, null, ex );
            Assert.assertTrue( ex instanceof ProvisioningException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

    }

}
