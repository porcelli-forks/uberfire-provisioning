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

package org.uberfire.provisioning.runtime;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.uberfire.provisioning.config.RuntimeConfig;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.*;

/**
 * @author salaboy This class represent the Container instance information,
 *         which might describe how to talk with the application/image instance and
 *         which features are provided
 */
@JsonTypeInfo(use = CLASS, include = WRAPPER_OBJECT)
public interface RuntimeInfo {

    String getId();

    String getName();

    RuntimeConfig getConfig();
}
