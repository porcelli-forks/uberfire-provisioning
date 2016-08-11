package org.uberfire.provisioning.source.git.config;

import org.uberfire.provisioning.config.SourceConfig;

public interface GitConfig extends SourceConfig {

    default String getRepoName() {
        return "${input.repo-name}";
    }

    default String getOrigin() {
        return "${input.origin}";
    }

    default String getBranch() {
        return "${input.branch}";
    }

    default String getOutPath() {
        return "${input.out-dir}";
    }
}
