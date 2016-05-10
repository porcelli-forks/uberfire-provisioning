/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.uberfire.provisioning.build.spi.Project;
import org.uberfire.provisioning.build.spi.exceptions.BuildException;

/**
 *
 * @author salaboy
 */
public class SimplePackageTest {
    
    public SimplePackageTest() {
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
    public void hello() {
        MavenBuild maven = new MavenBuild();
        Project mavenProject = new MavenProject();
        
        int build = 0;
        try {
            build = maven.build(mavenProject);
        } catch (BuildException ex) {
            Logger.getLogger(SimplePackageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Build: "+ build);
    }
}
