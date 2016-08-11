package org.uberfire.provisioning.build.maven.model;

import java.util.Map;

public interface PlugIn {

    String getId();

    Map<String, ?> getConfiguration();

}
