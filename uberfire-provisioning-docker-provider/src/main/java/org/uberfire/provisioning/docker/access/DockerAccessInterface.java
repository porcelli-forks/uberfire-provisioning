package org.uberfire.provisioning.docker.access;

import com.spotify.docker.client.DockerClient;
import org.uberfire.commons.lifecycle.Disposable;
import org.uberfire.provisioning.runtime.providers.ProviderId;

/**
 * TODO: update me
 */
public interface DockerAccessInterface extends Disposable {

    DockerClient getDockerClient( final ProviderId providerId );

}
