/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
package org.uberfire.provisioning.remote.client.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.remote.client.RestClientRuntimeProvisioningService;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfBuilder;
import org.uberfire.provisioning.docker.runtime.provider.DockerRuntimeConfBuilder;
import org.uberfire.provisioning.local.runtime.provider.LocalProviderConfBuilder;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntimeConfBuilder;
import org.uberfire.provisioning.local.runtime.provider.LocalRuntimeConfiguration;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftProviderConfBuilder;
import org.uberfire.provisioning.openshift.runtime.provider.OpenshiftRuntimeConfBuilder;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.providers.Provider;
import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.services.exceptions.BusinessException;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfBuilder;
import org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyRuntimeConfBuilder;

@RunWith(Arquillian.class)
public class RestProvisioningCDITest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(RestClientRuntimeProvisioningService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    private RestClientRuntimeProvisioningService provisioningService;

    @Test
    @Ignore // Need to deploy the services using arquillian to test the remote endpoints. 
    public void testProvisioningInjectionCDI() throws BusinessException {

        List<ProviderType> allProviderTypes = provisioningService.getAllProviderTypes();

        assertEquals(4, allProviderTypes.size());
        List<Provider> allProviders = provisioningService.getAllProviders();
        assertEquals(0, allProviders.size());

        ProviderConfiguration localConf = LocalProviderConfBuilder.newConfig("local runtime").get();
        provisioningService.registerProvider(localConf);
        
        ProviderConfiguration wildfly10Conf = WildflyProviderConfBuilder
                                                                .newConfig("wildfly on localhost")
                                                                .setHost("localhost")
                                                                .setPort("9990")
                                                                .setUser("admin")
                                                                .setPassword("admin")
                                                                .get();
        provisioningService.registerProvider(wildfly10Conf);
        

        ProviderConfiguration openshiftConf = OpenshiftProviderConfBuilder
                                                                .newConfig("openshift with oc logged in")
                                                                .get();
        provisioningService.registerProvider(openshiftConf);
        
        ProviderConfiguration dockerConf = DockerProviderConfBuilder
                                                                .newConfig("docker with local connection")
                                                                .get();
        provisioningService.registerProvider(dockerConf);
        
        allProviders = provisioningService.getAllProviders();
        assertEquals(4, allProviders.size());
        
        LocalRuntimeConfiguration localRuntimeConfig = LocalRuntimeConfBuilder.newConfig().setProviderName("local runtime")
                .setJar( "../extras/sample-war/target/sample-war-1.0-SNAPSHOT-swarm.jar" )
                .get();

      
        try {
            String newLocalRuntime = provisioningService.newRuntime( localRuntimeConfig );
            Assert.assertNotNull( newLocalRuntime );

        } catch ( Exception ex ) {
            Logger.getLogger(RestProvisioningCDITest.class.getName() ).log( Level.SEVERE, null, ex );
            Assert.assertTrue( ex instanceof BusinessException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

        RuntimeConfiguration wildflyRuntimeConfig = WildflyRuntimeConfBuilder.newConfig().setProviderName("wildfly on localhost")
                .setWarPath( "../extras/sample-war/target/sample-war-1.0-SNAPSHOT.war" )
                .get();

        
        try {
            String newWildflyRuntime = provisioningService.newRuntime( wildflyRuntimeConfig );
            Assert.assertNotNull( newWildflyRuntime );

        } catch ( Exception ex ) {
            Logger.getLogger(RestProvisioningCDITest.class.getName() ).log( Level.SEVERE, null, ex );
            Assert.assertTrue( ex instanceof BusinessException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

        RuntimeConfiguration openshiftRuntimeConfig = OpenshiftRuntimeConfBuilder.newConfig().setProviderName("openshift with oc logged in")
                .setNamespace( "default" )
                .setReplicationController( "test" )
                .setLabel( "uberfire" )
                .setServiceName( "test" )
                .setImage( "kitematic/hello-world-nginx" )
                .get();

        
        try {
            String newOpenshiftRuntime = provisioningService.newRuntime( openshiftRuntimeConfig );
            
            Assert.assertNotNull( newOpenshiftRuntime );
            
        } catch ( Exception ex ) {
            Logger.getLogger(RestProvisioningCDITest.class.getName() ).log( Level.SEVERE, null, ex );
            Assert.assertTrue( ex instanceof BusinessException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }

        RuntimeConfiguration dockerRuntimeConfig = DockerRuntimeConfBuilder.newConfig().setProviderName("docker with local connection")
                .setPull( true )
                .setImage( "kitematic/hello-world-nginx" )
                .get();

        try {
            String newDockerRuntime = provisioningService.newRuntime( dockerRuntimeConfig );
            Assert.assertNotNull( newDockerRuntime );
            
            
        } catch ( Exception ex ) {
            Logger.getLogger(RestProvisioningCDITest.class.getName() ).log( Level.SEVERE, null, ex );
            Assert.assertTrue( ex instanceof BusinessException );
            // If we get to this point something failed at creating a runtime, so it might be not configured.
        }
        
    }

}
