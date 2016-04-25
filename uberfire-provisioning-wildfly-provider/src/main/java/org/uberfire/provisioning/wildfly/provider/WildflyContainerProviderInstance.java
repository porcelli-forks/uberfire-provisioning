/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.wildfly.provider;

import javax.ws.rs.client.Client;

import org.uberfire.provisioning.spi.ContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProviderInstance;
import org.uberfire.provisioning.spi.providers.info.ContainerInstanceInfoImpl;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInstanceInfo;

/**
 *
 * @author salaboy
 */
public class WildflyContainerProviderInstance extends BaseContainerProviderInstance {

    private Client client;

    public WildflyContainerProviderInstance(ContainerProviderInstanceInfo cpi, ContainerInstanceConfiguration config) {
        super("Wildfly Client Provider", "was");
        System.out.println(">>> New WildflyContainerProviderInstance Instance... " + this.hashCode());

        this.config = config;
        this.containerProviderInstanceInfo = cpi;
        String host = cpi.getConfig().getProperties().get("host");
        String port = cpi.getConfig().getProperties().get("port");

    }

    @Override
    public ContainerInstanceInfo create() {

        String appName = config.getProperties().get("name");
        String warPath = config.getProperties().get("warPath");
        String target = config.getProperties().get("target");
        String user = config.getProperties().get("user");
        String password = config.getProperties().get("password");

        System.out.println("Creating container with user: " + user);
        System.out.println("Creating container with password: " + password);
        System.out.println("Creating container with target: " + target);
        System.out.println("Creating container with warPath: " + warPath);

//        ModelControllerClient client = null;
//
//        client = ModelControllerClient.Factory.create(
//                "localhost", 9990, new AuthCallbackHandler(user, password)
//        );
//
//        response = client.execute(Operations.createOperation("whoami"));
//
//        System.out.println("Response : " + response);
//        client = ClientBuilder.newClient();
//        
//
//        
//        Form form = new Form();
//        form.param("file", warPath);
//        Invocation.Builder builder = client.target(target).path("addcontent").request(MediaType.APPLICATION_JSON);
//        
//        Response response = builder
//                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
//        
//        System.out.println(">> Response: "+ response.getStatus());
//        
//        if(response.getStatus() == 401){
//            String digestHeader = response.getHeaderString("WWW-Authenticate");
//            builder = client.target(target).path("addcontent").request(MediaType.APPLICATION_JSON)
//                    .property(, name)
//                    .header("Authorization", digestHeader);    
//            response = builder
//                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
//            System.out.println(">> Response: "+ response.getStatus());
//        }
//        
        containerInstanceInfo = new ContainerInstanceInfoImpl(appName, appName, config);

        return containerInstanceInfo;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

}
