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

package org.uberfire.provisioning.wildfly.runtime.extras.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.uberfire.provisioning.wildfly.runtime.provider.extras.Wildfly10RemoteClient;

import static java.lang.System.*;


public class Wildfly10RemoteDeploymentTest {

    public Wildfly10RemoteDeploymentTest() {
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
    @Ignore
    /*
     *   This test requires a running wildfly and a path to an existing WAR
    */

    public void hello() {
        Wildfly10RemoteClient wildfly10RemoteClient = new Wildfly10RemoteClient();
        String filePath = "";

        int result = wildfly10RemoteClient.deploy( "<Username>", "<Password>", "localhost", 9990, filePath );
        out.println( "Result: " + result );

    }

}
