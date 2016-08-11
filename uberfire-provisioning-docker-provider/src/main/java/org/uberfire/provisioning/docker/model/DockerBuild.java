package org.uberfire.provisioning.docker.model;

import java.util.List;

import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.model.MavenBuild;

/**
 * TODO: update me
 */
public class DockerBuild extends MavenBuild {

    public DockerBuild( final Project project,
                        final List<String> goals ) {
        super( project, goals );
    }

}
