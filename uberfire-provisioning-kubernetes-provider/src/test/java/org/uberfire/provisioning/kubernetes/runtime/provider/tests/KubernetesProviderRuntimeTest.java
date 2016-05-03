package org.uberfire.provisioning.kubernetes.runtime.provider.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import io.fabric8.kubernetes.client.KubernetesClientException;
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
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProvider;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProviderType;
import org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesRuntime;

import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;

/**
 *
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class KubernetesProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(KubernetesProviderType.class)
                .addClass(KubernetesProvider.class)
                .addClass(KubernetesRuntime.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    @Any
    private Instance<ProviderType> providerTypes;

    public KubernetesProviderRuntimeTest() {
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
            Assert.assertEquals("kubernetes", pt.getProviderTypeName());
            Assert.assertEquals("1", pt.getVersion());
            i++;
        }
        Assert.assertEquals(1, i);

    }

    @Test
    public void newKubeProviderWithoutKubeClientSetupTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        BaseProviderConfiguration config = new BaseProviderConfiguration();
        KubernetesProvider kubernetesProvider = new KubernetesProvider(config, dockerProviderType);

        Assert.assertNotNull(kubernetesProvider.getKubernetes());
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put("namespace", "default");
        runtimeConfig.getProperties().put("replicationController", "test");
        runtimeConfig.getProperties().put("label", "uberfire");
        runtimeConfig.getProperties().put("serviceName", "test");
        runtimeConfig.getProperties().put("image", "kitematic/hello-world-nginx");

        Runtime newRuntime;
        try {
            newRuntime = kubernetesProvider.create(runtimeConfig);
        } catch (Exception ex) {
            // If the kubernetes  is not running and the system variables for locating the
            //   kubernetes deamon are not set, this is expected to fail.
            // If you are openshift origin you need to be logged in with the remote client
            //  for the kubernetes-api to pick up your configuration
            Assert.assertTrue(ex instanceof KubernetesClientException);
            Assert.assertTrue(((KubernetesClientException) ex).getMessage().contains("Failure executing: GET") || 
                    ((KubernetesClientException) ex).getMessage().contains("Error executing: GET"));
        }
        
        

    }
    @Test
    @Ignore
    public void newWildflyProviderWithWildflyRunningTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        BaseProviderConfiguration config = new BaseProviderConfiguration();
        
        
        KubernetesProvider kubernetesProvider = new KubernetesProvider(config, dockerProviderType);

        Assert.assertNotNull(kubernetesProvider.getKubernetes());
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        runtimeConfig.getProperties().put("namespace", "default");
        runtimeConfig.getProperties().put("replicationController", "test");
        runtimeConfig.getProperties().put("label", "uberfire");
        runtimeConfig.getProperties().put("serviceName", "test");
        runtimeConfig.getProperties().put("image", "kitematic/hello-world-nginx");


        Runtime newRuntime = kubernetesProvider.create(runtimeConfig);
        
        Assert.assertNotNull(newRuntime);
        Assert.assertNotNull(newRuntime.getId());

    }
}
