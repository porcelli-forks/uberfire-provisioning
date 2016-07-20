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

package org.uberfire.provisioning.pipeline.simple.provider;

import java.util.HashMap;
import java.util.Map;
import org.uberfire.provisioning.pipeline.PipelineDataContext;

public class PipelineDataContextImpl implements PipelineDataContext {

    private Map<String, Object> data;

    public void setData( String key, Object value ) {
        if ( data == null ) {
            data = new HashMap<>();
        }
        data.put( key, value );
    }

    public Object getData( String key ) {
        return data.get( key );
    }

    @Override
    public String toString() {
        return "PipelineResultsImpl{" + "data=" + data + '}';
    }

}
