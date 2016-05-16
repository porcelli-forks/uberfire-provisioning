package org.uberfire.provisioning.local.runtime.provider.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.local.runtime.provider.LocalProvider;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderConfiguration;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderType;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntime;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.exception.ProvisioningException;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;

/**
 *
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class LocalProviderRuntimeTest {
    
    @Deployment
    public static JavaArchive createDeployment() {
        
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(LocalProviderType.class)
                .addClass(LocalProvider.class)
                .addClass(LocalRuntime.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
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
        for (ProviderType pt : providerTypes) {
            Assert.assertEquals("local", pt.getProviderTypeName());
            Assert.assertEquals("1.0", pt.getVersion());
            i++;
        }
        Assert.assertEquals(1, i);
        
    }
    
    @Test
    public void newLocalProviderTest() {
        ProviderType localProviderType = providerTypes.iterator().next();
        LocalProviderConfiguration config = new LocalProviderConfiguration("local");
        LocalProvider localProvider = new LocalProvider(config, localProviderType);
        
        LocalRuntimeConfiguration runtimeConfig = new LocalRuntimeConfiguration();
        runtimeConfig.setJar("../extras/sample-war/target/sample-war-1.0-SNAPSHOT-swarm.jar");
        
        Runtime newRuntime;
        try {
            newRuntime = localProvider.create(runtimeConfig);
            newRuntime.start();
            Thread.sleep(20000);
            newRuntime.stop();
        } catch (ProvisioningException | InterruptedException ex) {
            
        }
        
    }
    
}
