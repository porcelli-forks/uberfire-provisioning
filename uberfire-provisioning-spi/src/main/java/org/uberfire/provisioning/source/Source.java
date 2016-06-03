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

package org.uberfire.provisioning.source;

import org.uberfire.provisioning.exceptions.SourcingException;

/**
 * @author salaboy
 *         <p>
 *         The Source interface provides all the methods to obtain and manage the source code that
 *         will be used by the other services.
 */
public interface Source {

    /*
    * Retrieve the source code from a Repository 
    * returns the location (path) of the obtained code
    * @throws SourcingException if the repository cannot  be located or the code cannot be retrieved
    * @param repository a source code Repository to use as Source for our projects
    * @return a String with the path to the retrieved source code
    * @see Repository
    */
    String getSource( Repository repository ) throws SourcingException;

    /*
    * Clean the source code obtained from a repository 
    * @throws SourcingException if there is an issue trying to clean the repository local path
    * @param repository a repository to clean
    * @return true if the source was cleaned, false if it didn't exist
    * @see Repository
    */
    boolean cleanSource( String sourcePath ) throws SourcingException;

}
