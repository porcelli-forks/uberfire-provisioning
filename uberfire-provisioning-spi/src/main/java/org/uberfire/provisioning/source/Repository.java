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

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.*;

/**
 * @author salaboy
 *         Type to represent a Source Code Repository
 */
@JsonTypeInfo(use = CLASS, include = WRAPPER_OBJECT)
public interface Repository {

    String getId();

    String getURI();

    String getName();

    String getType();

    String getBranch();

    boolean isBare();

    void setURI( String URI );

    void setName( String name );

    void setType( String type );

    void setBare( boolean bare );

    void setBranch( String branch );

}
