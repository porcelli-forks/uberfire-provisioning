package org.uberfire.provisioning.pipeline;

import org.uberfire.provisioning.config.Config;

public interface PipelineBuilder<INPUT extends Config, OUTPUT extends Config> {

    <T extends Config> PipelineBuilder<INPUT, T> andThen( final Stage<? super OUTPUT, T> nextStep );

    Pipeline buildAs( final String name );
}
