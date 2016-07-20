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

package org.uberfire.provisioning.registry.local.tests;

import org.uberfire.provisioning.runtime.providers.base.BaseProviderType;

public class MyProviderType extends BaseProviderType {

    public MyProviderType( String providerName,
            String version ) {
        super( providerName, version, MyProvider.class, MyProviderService.class );
    }

    public MyProviderType() {
        this( "my provider", "1.0" );
    }

}
