/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.util.wildfly8;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.uberfire.provisioning.wildfly.util.Wildfly10RemoteClient;

/**
 *
 * @author salaboy
 */
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

        int result = wildfly10RemoteClient.deploy("<Username>", "<Password>", "localhost", 9990, filePath);
        System.out.println("Result: " + result);

    }

}
