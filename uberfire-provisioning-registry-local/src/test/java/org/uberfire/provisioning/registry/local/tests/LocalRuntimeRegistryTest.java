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

import java.util.List;
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
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.registry.local.InMemoryRuntimeRegistry;
import org.uberfire.provisioning.runtime.Runtime;
import org.uberfire.provisioning.runtime.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderType;

import static java.lang.System.*;
import static java.util.UUID.*;
import static org.jboss.shrinkwrap.api.ShrinkWrap.*;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.*;
import static org.junit.Assert.*;

/**
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class LocalRuntimeRegistryTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = create( JavaArchive.class )
                .addClass( InMemoryRuntimeRegistry.class )
                .addAsManifestResource( INSTANCE, "beans.xml" );
        out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    private RuntimeRegistry registry;

    public LocalRuntimeRegistryTest() {
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
    public void simpleLocalRegistryProviderTypesTest() {

        assertNotNull( registry );
        MyProviderType myProviderType = new MyProviderType( "test provider type", "1.0" );
        registry.registerProviderType( myProviderType );

        ProviderType pt = registry.getProviderTypeByName( myProviderType.getProviderTypeName() );
        assertNotNull( pt );

        List<ProviderType> types = registry.getAllProviderTypes();
        assertEquals( 1, types.size() );

        registry.unregisterProviderType( myProviderType );

        types = registry.getAllProviderTypes();
        assertEquals( 0, types.size() );
    }

    @Test
    public void simpleLocalRegistryProvidersTest() {

        assertNotNull( registry );
        MyProviderType myProviderType = new MyProviderType( "test provider type", "1.0" );

        registry.registerProviderType( myProviderType );

        MyProvider provider = new MyProvider( "my provider instance", myProviderType );

        registry.registerProvider( provider );

        List<Provider> providers = registry.getAllProviders();
        assertEquals( 1, providers.size() );

        List<Provider> providersByType = registry.getProvidersByType( myProviderType );
        assertEquals( 1, providersByType.size() );

        registry.unregisterProvider( provider );

        providers = registry.getAllProviders();
        assertEquals( 0, providers.size() );

        providersByType = registry.getProvidersByType( myProviderType );
        assertEquals( 0, providersByType.size() );

    }

    @Test
    public void simpleLocalRegistryRuntimeTest() {

        assertNotNull( registry );
        MyProviderType myProviderType = new MyProviderType( "test provider type", "1.0" );

        registry.registerProviderType( myProviderType );

        MyProvider provider = new MyProvider( "my provider instance", myProviderType );

        registry.registerProvider( provider );
        String id = randomUUID().toString();
        BaseRuntimeConfiguration baseConf = new BaseRuntimeConfiguration();

        MyRuntime myRuntime = new MyRuntime( id, baseConf, provider );
        registry.registerRuntime( myRuntime );

        List<Runtime> allRuntimes = registry.getAllRuntimes();
        assertEquals( 1, allRuntimes.size() );

        List<Runtime> runtimesByProvider = registry.getRuntimesByProvider( myProviderType );
        assertEquals( 1, runtimesByProvider.size() );

        registry.unregisterRuntime( myRuntime );
        allRuntimes = registry.getAllRuntimes();
        assertEquals( 0, allRuntimes.size() );

        runtimesByProvider = registry.getRuntimesByProvider( myProviderType );
        assertEquals( 0, runtimesByProvider.size() );

    }

}
