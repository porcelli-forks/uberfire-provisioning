package org.uberfire.provisioning.wildfly.runtime.provider.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.http.conn.HttpHostConnectException;
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
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.WildflyProvider;
import org.uberfire.provisioning.wildfly.runtime.provider.WildflyProviderType;
import org.uberfire.provisioning.wildfly.runtime.provider.WildflyRuntime;

/**
 *
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class WildflyProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(WildflyProviderType.class)
                .addClass(WildflyProvider.class)
                .addClass(WildflyRuntime.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    public WildflyProviderRuntimeTest() {
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
            Assert.assertEquals("wildfly", pt.getProviderTypeName());
            Assert.assertEquals("10.0.0", pt.getVersion());
            i++;
        }
        Assert.assertEquals(1, i);

    }

    @Test
    public void newWildflyProviderWithoutWildflyRunningTest() {
        ProviderType wildlyProviderType = providerTypes.iterator().next();
        BaseProviderConfiguration config = new BaseProviderConfiguration();
        
        config.getProperties().put("host", "localhost");
        config.getProperties().put("port", "9990");
        config.getProperties().put("user", "someuser");
        config.getProperties().put("password", "somepassword");
        
        WildflyProvider wildflyProvider = new WildflyProvider(config, wildlyProviderType);

        Assert.assertNotNull(wildflyProvider.getWildfly());
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put("warPath", "");

        Runtime newRuntime = null;
        
        try {
            newRuntime = wildflyProvider.create(runtimeConfig);
        } catch (Exception ex) {
            // If the wildfly is not running this is expected to fail.
            Assert.assertTrue(ex instanceof HttpHostConnectException);
            Assert.assertTrue(ex.getMessage().contains("Connection refused"));
        }

    }
    
    
    
    @Test
    @Ignore
    public void newWildflyProviderWithWildflyRunningTest() {
        ProviderType wildflyProviderType = providerTypes.iterator().next();
        BaseProviderConfiguration config = new BaseProviderConfiguration();
        
        config.getProperties().put("host", "localhost");
        config.getProperties().put("port", "9990");
        config.getProperties().put("user", "salaboy");
        config.getProperties().put("password", "salaboy123$");
        
        WildflyProvider wildflyProvider = new WildflyProvider(config, wildflyProviderType);

        Assert.assertNotNull(wildflyProvider.getWildfly());
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put("warPath", "/Users/salaboy/Projects/uberfire-provisioning/sample-war/target/sample-war-1.0-SNAPSHOT.war");

        Runtime newRuntime;
      
        newRuntime = wildflyProvider.create(runtimeConfig);
        
        Assert.assertNotNull(newRuntime);
        Assert.assertNotNull(newRuntime.getId());

    }


}
