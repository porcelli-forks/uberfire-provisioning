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

package org.uberfire.provisioning.build.maven;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.exceptions.BuildException;

import static java.lang.System.*;
import static java.util.logging.Level.*;
import static java.util.logging.Logger.*;

/**
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
    /*
     * this test is ignored because it requires you to get the M2_HOME set up in your environment
    */
    public void hello() {
        MavenBuild maven = new MavenBuild();
        Project mavenProject = new MavenProject();

        int build = 0;
        try {
            build = maven.build( mavenProject );
        } catch ( BuildException ex ) {
            getLogger( SimplePackageTest.class.getName() ).log( SEVERE, null, ex );
        }

        out.println( "Build: " + build );
    }

    @Test
    @Ignore
    /*
     * this test is ignored because it requires you to get the M2_HOME set up 
     * and the docker client running in your env.
    */
    public void helloWithDocker() {
        MavenBuild maven = new MavenBuild();
        Project mavenProject = new MavenProject();

        int dockerImageBuild = 0;
        try {
            dockerImageBuild = maven.createDockerImage( mavenProject );
        } catch ( BuildException ex ) {
            getLogger( SimplePackageTest.class.getName() ).log( SEVERE, null, ex );
        }

        out.println( "Docker: " + dockerImageBuild );
    }
}
