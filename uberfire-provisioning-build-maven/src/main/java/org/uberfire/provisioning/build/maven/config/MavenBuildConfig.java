package org.uberfire.provisioning.build.maven.config;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.provisioning.config.BuildConfig;

public interface MavenBuildConfig extends BuildConfig {

    default List<String> getGoals() {
        final List<String> result = new ArrayList<>();
        result.add( "package" );
        result.add( "-DfailIfNoTests=false" );
        return result;
    }

}
