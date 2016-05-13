package org.uberfire.provisioning.wildfly.runtime.provider.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.exception.ProvisioningException;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfiguration;
import org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10ProviderType;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntime;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeConfiguration;

/**
 *
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class WildflyProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(Wildfly10ProviderType.class)
                .addClass(Wildfly10Provider.class)
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
        WildflyProviderConfiguration config = new WildflyProviderConfiguration("wildfly @ 9990");

        config.setHost("localhost");
        config.setManagementPort("9990");
        config.setUser("someuser");
        config.setPassword("somepassword");

        Wildfly10Provider wildflyProvider = new Wildfly10Provider(config, wildlyProviderType);

        Assert.assertNotNull(wildflyProvider.getWildfly());
        WildflyRuntimeConfiguration runtimeConfig = new WildflyRuntimeConfiguration();
        runtimeConfig.setWarPath("");

        Runtime newRuntime = null;

        try {
            newRuntime = wildflyProvider.create(runtimeConfig);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof ProvisioningException);
        }

    }

    @Test
    @Ignore
    public void newWildflyProviderWithWildflyRunningTest() {
        ProviderType wildflyProviderType = providerTypes.iterator().next();
        
        WildflyProviderConfiguration config = new WildflyProviderConfiguration("wildfly @ 9990");

        config.setHost("localhost");
        config.setManagementPort("9990");
        config.setUser("someuser");
        config.setPassword("somepassword");

        Wildfly10Provider wildflyProvider = new Wildfly10Provider(config, wildflyProviderType);

        Assert.assertNotNull(wildflyProvider.getWildfly());
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put("warPath", "/Users/salaboy/Projects/uberfire-provisioning/sample-war/target/sample-war-1.0-SNAPSHOT.war");

        Runtime newRuntime = null;

        try {
            newRuntime = wildflyProvider.create(runtimeConfig);
        } catch (ProvisioningException ex) {
            Logger.getLogger(WildflyProviderRuntimeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Assert.assertNotNull(newRuntime);
        Assert.assertNotNull(newRuntime.getId());

    }

}
