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

package org.uberfire.provisioning.build;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.uberfire.java.nio.file.Path;
import org.uberfire.provisioning.config.BinaryConfig;

/**
 * @author salaboy
 *         Generic Binary type used to store information about the generated binaries.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface Binary extends BinaryConfig {

    Project getProject();

    Path getPath();

    String getType();

    String getName();

}
