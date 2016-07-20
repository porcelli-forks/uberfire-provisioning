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

package org.uberfire.provisioning.pipeline;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseStage implements Stage {

    private String name;
    private Set<Class> requiredServices = new HashSet<>();

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Class> getRequiredServices() {
        return requiredServices;
    }

    @Override
    public void setRequiredServices( Set<Class> requiredServices ) {
        this.requiredServices = requiredServices;
    }

    @Override
    public void addRequiredService( Class type ) {
        if ( requiredServices == null ) {
            requiredServices = new HashSet<>();
        }
        this.requiredServices.add( type );
    }

}
