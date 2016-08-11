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

package org.uberfire.provisioning.docker.model;

import org.uberfire.provisioning.config.RuntimeConfig;
import org.uberfire.provisioning.runtime.base.BaseRuntime;
import org.uberfire.provisioning.runtime.providers.ProviderId;

public class DockerRuntime extends BaseRuntime {

    public DockerRuntime( final String id,
                          final RuntimeConfig config,
                          final ProviderId providerId ) {
        super( id, config, providerId );
    }

    @Override
    public String toString() {
        return "DockerRuntime{" + getId() + " }";
    }
}
