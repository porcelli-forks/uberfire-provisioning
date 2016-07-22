
package org.uberfire.provisioning.services.swarm.tests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

@RunWith( Arquillian.class )
public class EndpointsTest {

    @Deployment( testable = true )
    public static Archive createDeployment() throws Exception {
        JAXRSArchive deployment = ShrinkWrap.create( JAXRSArchive.class );
        deployment.addPackages( true, "com.google.common" );
        deployment.setContextRoot( "/api" );
        deployment.addAllDependencies();
        return deployment;
    }

    @Test
    @RunAsClient
    public void checkService() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target( "http://localhost:8080/providertypes" );
        Response response = target.request( MediaType.APPLICATION_JSON ).get();
        Assert.assertEquals( Response.Status.OK.getStatusCode(), response.getStatus() );
        
        String responseAsString = response.readEntity(String.class);
        assertNotNull(responseAsString);
        
    }

}
