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

package org.uberfire.provisioning.runtime.base;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.uberfire.provisioning.runtime.RuntimeConfiguration;

public class BaseRuntimeConfiguration implements RuntimeConfiguration {

    @JsonIgnore
    private Map<String, String> properties = new HashMap<>();

    private String providerName;

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties( Map<String, String> props ) {
        this.properties = props;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public void setProviderName( String providerName ) {
        this.providerName = providerName;
    }

    public void setContext( String context ) {
        getProperties().put( "context", context );
    }
}
