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

package org.uberfire.provisioning.local.runtime.provider.tests;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.exceptions.ProvisioningException;
import org.uberfire.provisioning.local.runtime.provider.LocalProvider;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderConfiguration;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderType;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntime;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntimeConfiguration;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.providers.ProviderType;

import static java.lang.System.*;
import static java.lang.Thread.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.*;
import static org.junit.Assert.*;

/**
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class LocalProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = create( JavaArchive.class )
                .addClass( LocalProviderType.class )
                .addClass( LocalProvider.class )
                .addClass( LocalRuntime.class )
                .addAsManifestResource( INSTANCE, "beans.xml" );
        out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    public LocalProviderRuntimeTest() {
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
            assertEquals( "local", pt.getProviderTypeName() );
            assertEquals( "1.0", pt.getVersion() );
            i++;
        }
        assertEquals( 1, i );

    }

    @Test
    public void newLocalProviderTest() {
        ProviderType localProviderType = providerTypes.iterator().next();
        LocalProviderConfiguration config = new LocalProviderConfiguration( "local" );
        LocalProvider localProvider = new LocalProvider( config, localProviderType );

        LocalRuntimeConfiguration runtimeConfig = new LocalRuntimeConfiguration();
        runtimeConfig.setJar( "../extras/sample-war/target/sample-war-1.0-SNAPSHOT-swarm.jar" );

        Runtime newRuntime;
        try {
            newRuntime = localProvider.create( runtimeConfig );
            newRuntime.start();
            sleep( 20000 );
            newRuntime.stop();
        } catch ( ProvisioningException | InterruptedException ex ) {

        }

    }

}
