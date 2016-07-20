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

package org.uberfire.provisinion.docker.runtime.provider.test;

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
import org.uberfire.provisioning.docker.runtime.provider.DockerProvider;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfiguration;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderType;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntime;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntimeConfiguration;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;

import static java.lang.System.*;
import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.*;
import static org.junit.Assert.*;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderService;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntimeService;

/**
 * @author salaboy
 */
@RunWith( Arquillian.class )
public class DockerProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = create( JavaArchive.class )
                .addClass( DockerProviderType.class )
                .addClass( DockerProvider.class )
                .addClass( DockerRuntime.class )
                .addAsManifestResource( INSTANCE, "beans.xml" );
        out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    public DockerProviderRuntimeTest() {
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
            assertEquals( "docker", pt.getProviderTypeName() );
            assertEquals( "1.9.1", pt.getVersion() );
            i++;
        }
        assertEquals( 1, i );

    }

    @Test
    public void newDockerProviderWithoutDockerClientRunningTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        DockerProviderConfiguration config = new DockerProviderConfiguration( "docker local deamon" );

        DockerProvider dockerProvider = new DockerProvider( config, dockerProviderType );

        DockerProviderService dockerProviderService = new DockerProviderService( dockerProvider );

        assertNotNull( dockerProviderService.getDocker() );
        DockerRuntimeConfiguration runtimeConfig = new DockerRuntimeConfiguration();
        runtimeConfig.setImage( "kitematic/hello-world-nginx" );

        Runtime newRuntime;
        try {
            newRuntime = dockerProviderService.create( runtimeConfig );
        } catch ( Exception ex ) {
            // If the docker deamon is not running and the system variables for locating the
            //   docker deamon are not set, this is expected to fail.
            assertTrue( ex instanceof ProvisioningException );
        }

    }

    @Test
    @Ignore
    public void newDockerProviderWithDockerClientRunningTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        DockerProviderConfiguration config = new DockerProviderConfiguration( "docker local deamon" );
        
        DockerProvider dockerProvider = new DockerProvider( config, dockerProviderType );
        
        DockerProviderService dockerProviderService = new DockerProviderService( dockerProvider );

        assertNotNull( dockerProviderService.getDocker() );
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        //Notice that it will not pull images that you don't have locally 
        // The hello-world-nginx is a very small image to test with, 
        //   but you might need to pull it first
        runtimeConfig.getProperties().put( "name", "kitematic/hello-world-nginx" );

        Runtime newRuntime = null;
        try {
            newRuntime = dockerProviderService.create( runtimeConfig );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            fail( "Docker client is not configured" );

        }
        assertNotNull( newRuntime );
        DockerRuntimeService dockerRuntimeService = new DockerRuntimeService( dockerProviderService, ( DockerRuntime ) newRuntime );

        dockerRuntimeService.start();
        
        assertEquals( "Running", newRuntime.getState().getStatus() );
        
        newRuntime = dockerRuntimeService.getRuntime();
        
        dockerRuntimeService.refresh();
        
        newRuntime = dockerRuntimeService.getRuntime();

        dockerRuntimeService.refresh();

        dockerRuntimeService.stop();

        try {
            dockerProviderService.destroy( newRuntime.getId() );
        } catch ( Exception ex ) {
            getLogger( DockerProviderRuntimeTest.class.getName() ).log( SEVERE, null, ex );

        }

    }

}
