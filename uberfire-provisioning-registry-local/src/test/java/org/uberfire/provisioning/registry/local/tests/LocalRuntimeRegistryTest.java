package org.uberfire.provisioning.registry.local.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import java.util.UUID;
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
import org.uberfire.provisioning.registry.RuntimeRegistry;
import org.uberfire.provisioning.registry.local.InMemoryRuntimeRegistry;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

/**
 *
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class LocalRuntimeRegistryTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(InMemoryRuntimeRegistry.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
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

        Assert.assertNotNull(registry);
        MyProviderType myProviderType = new MyProviderType("test provider type", "1.0");
        registry.registerProviderType(myProviderType);

        ProviderType pt = registry.getProviderTypeByName(myProviderType.getProviderTypeName());
        Assert.assertNotNull(pt);

        List<ProviderType> types = registry.getAllProviderTypes();
        Assert.assertEquals(1, types.size());

        registry.unregisterProviderType(myProviderType);

        types = registry.getAllProviderTypes();
        Assert.assertEquals(0, types.size());
    }

    @Test
    public void simpleLocalRegistryProvidersTest() {

        Assert.assertNotNull(registry);
        MyProviderType myProviderType = new MyProviderType("test provider type", "1.0");

        registry.registerProviderType(myProviderType);

        MyProvider provider = new MyProvider("my provider instance", myProviderType);

        registry.registerProvider(provider);

        List<Provider> providers = registry.getAllProviders();
        Assert.assertEquals(1, providers.size());

        List<Provider> providersByType = registry.getProvidersByType(myProviderType);
        Assert.assertEquals(1, providersByType.size());

        registry.unregisterProvider(provider);

        providers = registry.getAllProviders();
        Assert.assertEquals(0, providers.size());

        providersByType = registry.getProvidersByType(myProviderType);
        Assert.assertEquals(0, providersByType.size());

    }

    @Test
    public void simpleLocalRegistryRuntimeTest() {

        Assert.assertNotNull(registry);
        MyProviderType myProviderType = new MyProviderType("test provider type", "1.0");

        registry.registerProviderType(myProviderType);

        MyProvider provider = new MyProvider("my provider instance", myProviderType);

        registry.registerProvider(provider);
        String id = UUID.randomUUID().toString();
        BaseRuntimeConfiguration baseConf = new BaseRuntimeConfiguration();

        MyRuntime myRuntime = new MyRuntime(id, baseConf, provider);
        registry.registerRuntime(myRuntime);

        List<Runtime> allRuntimes = registry.getAllRuntimes();
        Assert.assertEquals(1, allRuntimes.size());

        List<Runtime> runtimesByProvider = registry.getRuntimesByProvider(myProviderType);
        Assert.assertEquals(1, runtimesByProvider.size());

        registry.unregisterRuntime(myRuntime);
        allRuntimes = registry.getAllRuntimes();
        Assert.assertEquals(0, allRuntimes.size());

        runtimesByProvider = registry.getRuntimesByProvider(myProviderType);
        Assert.assertEquals(0, runtimesByProvider.size());

    }

}
