package org.uberfire.provisioning.pipeline;

import org.uberfire.provisioning.config.Config;

/**
 * TODO: update me
 */
public interface ConfigExecutor {

    Class<? extends Config> executeFor();

    String outputId();

    default String inputId() {
        return "none";
    }

}
