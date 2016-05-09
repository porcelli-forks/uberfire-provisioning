/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.github;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.uberfire.provisioning.source.Source;

/**
 *
 * @author salaboy
 */
public class CloneTestJUnitTest {

    public CloneTestJUnitTest() {
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
    public void hello() throws Exception {
        Source source = new GitHubSource();
        String pathToRepo = source.getSource("https://github.com/pefernan/livespark-playground.git", "/tmp/livespark");
        
        
    }
}
