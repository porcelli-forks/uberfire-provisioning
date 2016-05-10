/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.github;

import java.io.File;
import java.nio.file.Files;
import org.eclipse.jgit.util.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uberfire.provisioning.source.Repository;
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
    public void hello() throws Exception {
        Source source = new GitSource();
        Repository gitHubRepository = new GitHubRepository("Livespark Playground");
        gitHubRepository.setURI("https://github.com/pefernan/livespark-playground.git");

        String destinationPath = source.getSource(gitHubRepository);
        System.out.println("TMP Created Dir: " + destinationPath);
        Assert.assertTrue(new File(destinationPath).isDirectory());

    }
}
