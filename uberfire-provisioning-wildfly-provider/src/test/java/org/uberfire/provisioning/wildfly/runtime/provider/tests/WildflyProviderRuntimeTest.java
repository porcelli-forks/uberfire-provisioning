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

package org.uberfire.provisioning.wildfly.runtime.provider.tests;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntime;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderType;

import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.*;
import static org.junit.Assert.*;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeService;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderService;

@RunWith( Arquillian.class )
public class WildflyProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = create( JavaArchive.class )
                .addClass( Wildfly10ProviderType.class )
                .addClass( Wildfly10Provider.class )
                .addClass( WildflyRuntime.class )
                .addAsManifestResource( INSTANCE, "beans.xml" );
        //System.out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    public WildflyProviderRuntimeTest() {
    }

    @Test
    public void providerTypeRegisteredTest() {
        int i = 0;
        for ( ProviderType pt : providerTypes ) {
            assertEquals( "wildfly", pt.getProviderTypeName() );
            assertEquals( "10.0.0", pt.getVersion() );
            i++;
        }
        assertEquals( 1, i );

    }

    @Test
    public void newWildflyProviderWithoutWildflyRunningTest() {
        ProviderType wildlyProviderType = providerTypes.iterator().next();
        WildflyProviderConfiguration config = new WildflyProviderConfiguration( "wildfly @ 9990" );

        config.setHost( "localhost" );
        config.setManagementPort( "9990" );
        config.setUser( "someuser" );
        config.setPassword( "somepassword" );

        Wildfly10Provider wildflyProvider = new Wildfly10Provider( config, wildlyProviderType );
        Wildfly10ProviderService providerService = new Wildfly10ProviderService( wildflyProvider );

        assertNotNull( providerService.getWildfly() );
        WildflyRuntimeConfiguration runtimeConfig = new WildflyRuntimeConfiguration();
        runtimeConfig.setWarPath( "" );

        Runtime newRuntime = null;

        try {
            newRuntime = providerService.create( runtimeConfig );
        } catch ( Exception ex ) {
            assertTrue( ex instanceof ProvisioningException );
        }

    }

    @Test
    @Ignore
    public void newWildflyProviderWithWildflyRunningTest() {
        ProviderType wildflyProviderType = providerTypes.iterator().next();

        WildflyProviderConfiguration config = new WildflyProviderConfiguration( "wildfly @ 9990" );

        config.setHost( "localhost" );
        config.setManagementPort( "9990" );
        config.setUser( "admin" );
        config.setPassword( "admin" );

        Wildfly10Provider wildflyProvider = new Wildfly10Provider( config, wildflyProviderType );
        Wildfly10ProviderService providerService = new Wildfly10ProviderService( wildflyProvider );

        assertNotNull( providerService.getWildfly() );
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put( "warPath", "../extras/sample-war/target/sample-war-1.0-SNAPSHOT.war" );

        Runtime newRuntime = null;

        try {
            newRuntime = providerService.create( runtimeConfig );
        } catch ( ProvisioningException ex ) {
            getLogger( WildflyProviderRuntimeTest.class.getName() ).log( SEVERE, null, ex );
        }

        assertNotNull( newRuntime );
        assertNotNull( newRuntime.getId() );

        WildflyRuntimeService wildflyRuntimeService = new WildflyRuntimeService( providerService, newRuntime );

        wildflyRuntimeService.refresh();

        newRuntime = wildflyRuntimeService.getRuntime();

        // TODO: check state and info
    }

}
