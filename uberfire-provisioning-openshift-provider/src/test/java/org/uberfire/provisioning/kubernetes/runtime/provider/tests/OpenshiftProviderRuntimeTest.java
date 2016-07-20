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

package org.uberfire.provisioning.kubernetes.runtime.provider.tests;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProvider;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfiguration;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderType;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntime;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntimeConfiguration;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;

import static java.lang.System.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.*;
import static org.junit.Assert.*;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfBuilder;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderService;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntimeService;
import org.uberfire.provisioning.runtime.RuntimeInfo;
import org.uberfire.provisioning.runtime.RuntimeService;
import org.uberfire.provisioning.runtime.RuntimeState;

@RunWith( Arquillian.class )
public class OpenshiftProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = create( JavaArchive.class )
                .addClass(OpenshiftProviderType.class )
                .addClass(OpenshiftProvider.class )
                .addClass(OpenshiftRuntime.class )
                .addAsManifestResource( INSTANCE, "beans.xml" );
        out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    public OpenshiftProviderRuntimeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void providerTypeRegisteredTest() {
        int i = 0;
        for ( ProviderType pt : providerTypes ) {
            assertEquals( "openshift", pt.getProviderTypeName() );
            assertEquals( "1", pt.getVersion() );
            i++;
        }
        assertEquals( 1, i );

    }

    @Test
    public void newOpenshiftProviderWithoutOpenshiftClientSetupTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        OpenshiftProviderConfiguration config = new OpenshiftProviderConfiguration( "openshift" );

        OpenshiftProvider kubernetesProvider = new OpenshiftProvider( config, dockerProviderType );

        OpenshiftProviderService kubernetesProviderService = new OpenshiftProviderService( kubernetesProvider );

        assertNotNull( kubernetesProviderService.getKubernetesClient() );
        OpenshiftRuntimeConfiguration runtimeConfig = new OpenshiftRuntimeConfiguration();
        runtimeConfig.setNamespace( "default" );
        runtimeConfig.setReplicationController( "test" );
        runtimeConfig.setLabel( "uberfire" );
        runtimeConfig.setServiceName( "test" );
        runtimeConfig.setInternalPort( "8080" );
        runtimeConfig.setImage( "kitematic/hello-world-nginx" );

        Runtime newRuntime;

        try {
            newRuntime = kubernetesProviderService.create( runtimeConfig );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            // If the kubernetes  is not running and the system variables for locating the
            //   kubernetes deamon are not set, this is expected to fail.
            // If you are openshift origin you need to be logged in with the remote client
            //  for the kubernetes-api to pick up your configuration
            assertTrue( ex instanceof ProvisioningException );

        }

    }

    @Test
    @Ignore // You need kubernetes or openshift (-vm) running for this test. 
    public void newOpenshiftProviderWithOpenshiftRunningTest() throws ProvisioningException {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        OpenshiftProviderConfiguration config = OpenshiftProviderConfBuilder.newConfig( "openshift" )
                .setMasterUrl( "https://10.2.2.2:8443/" ).setUsername( "admin" ).setPassword( "admin" ).get();

        OpenshiftProvider openshiftProvider = new OpenshiftProvider( config, dockerProviderType );

        OpenshiftProviderService openshiftProviderService = new OpenshiftProviderService( openshiftProvider );

        assertNotNull( openshiftProviderService.getKubernetesClient() );
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put( "namespace", "default" );
        runtimeConfig.getProperties().put( "replicationController", "test" );
        runtimeConfig.getProperties().put( "label", "uberfire" );
        runtimeConfig.getProperties().put( "serviceName", "test" );
        runtimeConfig.getProperties().put( "internalPort", "8080" );
        runtimeConfig.getProperties().put( "image", "kitematic/hello-world-nginx" );

        Runtime newRuntime = openshiftProviderService.create( runtimeConfig );

        assertNotNull( newRuntime );
        assertNotNull( newRuntime.getId() );

        RuntimeService runtimeService = new OpenshiftRuntimeService( openshiftProviderService, newRuntime );

        runtimeService.refresh();

        newRuntime = runtimeService.getRuntime();

        RuntimeState state = newRuntime.getState();
        RuntimeInfo info = newRuntime.getInfo();
        
        runtimeService.stop();
        
        newRuntime = runtimeService.getRuntime();
        
        state = newRuntime.getState();
        info = newRuntime.getInfo();
        
        openshiftProviderService.destroy( newRuntime.getId() );

    }
}
