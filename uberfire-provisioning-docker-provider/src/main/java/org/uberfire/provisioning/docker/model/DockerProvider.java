/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.model;

import org.uberfire.provisioning.config.ProviderConfig;
import org.uberfire.provisioning.runtime.providers.base.BaseProvider;

public class DockerProvider extends BaseProvider implements ProviderConfig {

    private final String hostId;

    public DockerProvider( final String name,
                           final String hostId ) {
        super( name, DockerProviderType.instance() );
        this.hostId = hostId;
    }

    public String getHostId() {
        return hostId;
    }
}
