package org.uberfire.provisioning.build.maven.model;

import java.util.Collection;

import org.uberfire.provisioning.build.Project;

public interface MavenProject extends Project {

    Collection<PlugIn> getBuildPlugins();

}
