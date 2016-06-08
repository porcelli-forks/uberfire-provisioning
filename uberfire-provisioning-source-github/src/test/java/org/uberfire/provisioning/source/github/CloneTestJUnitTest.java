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

import org.junit.Test;
import org.uberfire.provisioning.source.Source;

import static org.junit.Assert.*;

/**
 * @author salaboy
 */
public class CloneTestJUnitTest {

    public CloneTestJUnitTest() {
    }

    @Test
    public void hello() throws Exception {
        final GitHub gitHub = new GitHub();
        final GitHubRepository repository = (GitHubRepository) gitHub.getRepository( "pefernan/livespark-playground" );
        final Source source = repository.getSource( "master" );
        assertNotNull( source );
    }
}
