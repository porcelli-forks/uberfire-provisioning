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

package org.uberfire.provisioning.source.github;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;

import static java.lang.System.*;
import static org.junit.Assert.*;

/**
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
        Repository gitHubRepository = new GitHubRepository( "Livespark Playground" );
        gitHubRepository.setURI( "https://github.com/pefernan/livespark-playground.git" );

        String destinationPath = source.getSource( gitHubRepository );
        out.println( "TMP Created Dir: " + destinationPath );
        assertTrue( new File( destinationPath ).isDirectory() );

    }
}
