package org.uberfire.provisinion.docker.runtime.provider.test;

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
import org.uberfire.provisioning.docker.runtime.provider.DockerProvider;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfiguration;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderType;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntime;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.Runtime;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.base.BaseRuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.exception.ProvisioningException;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.runtime.spi.providers.base.BaseProviderConfiguration;

/**
 *
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class DockerProviderRuntimeTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(DockerProviderType.class)
                .addClass(DockerProvider.class)
                .addClass(DockerRuntime.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
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
        for (ProviderType pt : providerTypes) {
            Assert.assertEquals("docker", pt.getProviderTypeName());
            Assert.assertEquals("1.9.1", pt.getVersion());
            i++;
        }
        Assert.assertEquals(1, i);

    }

    @Test
    public void newDockerProviderWithoutDockerClientRunningTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        DockerProviderConfiguration config = new DockerProviderConfiguration("docker local deamon");
        DockerProvider dockerProvider = new DockerProvider(config, dockerProviderType);

        Assert.assertNotNull(dockerProvider.getDocker());
        DockerRuntimeConfiguration runtimeConfig = new DockerRuntimeConfiguration();
        runtimeConfig.setImage("kitematic/hello-world-nginx");

        Runtime newRuntime;
        try {
            newRuntime = dockerProvider.create(runtimeConfig);
        } catch (Exception ex) {
            // If the docker deamon is not running and the system variables for locating the
            //   docker deamon are not set, this is expected to fail.
            Assert.assertTrue(ex instanceof ProvisioningException);   
        }

    }

    @Test
    @Ignore
    public void newDockerProviderWithDockerClientRunningTest() {
        ProviderType dockerProviderType = providerTypes.iterator().next();
        DockerProviderConfiguration config = new DockerProviderConfiguration("docker local deamon");
        DockerProvider dockerProvider = new DockerProvider(config, dockerProviderType);

        Assert.assertNotNull(dockerProvider.getDocker());
        RuntimeConfiguration runtimeConfig = new BaseRuntimeConfiguration();
        //Notice that it will not pull images that you don't have locally 
        // The hello-world-nginx is a very small image to test with, 
        //   but you might need to pull it first
        runtimeConfig.getProperties().put("name", "kitematic/hello-world-nginx");

        Runtime newRuntime = null;
        try {
            newRuntime = dockerProvider.create(runtimeConfig);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Docker client is not configured");
            
        }
        Assert.assertNotNull(newRuntime);

        newRuntime.start();
        
        
        Assert.assertTrue(newRuntime.getState().isRunning());
  
        try {
            dockerProvider.destroy(newRuntime.getId());
        } catch (Exception ex) {
            Logger.getLogger(DockerProviderRuntimeTest.class.getName()).log(Level.SEVERE, null, ex);
            
        }

    }

}
