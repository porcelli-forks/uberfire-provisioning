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

package org.uberfire.provisioning.services;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
//import org.wildfly.swarm.keycloak.Secured;

/**
 * @author salaboy
 */
public class App {

    public static void main( String[] args ) throws Exception {
        Container container = new Container();

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create( JAXRSArchive.class );
//        deployment.as(Secured.class);
        deployment.setContextRoot( "/api" );
//        deployment.addModule("sun.jdk");
//        deployment.addPackages(true, "org.kie.container.services");
        deployment.addAsLibrary( container.createDefaultDeployment() );
        deployment.addAllDependencies();

        container.deploy( deployment );
    }
}
