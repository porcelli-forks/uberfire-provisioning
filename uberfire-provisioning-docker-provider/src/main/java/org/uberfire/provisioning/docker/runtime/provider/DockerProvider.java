/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.docker.runtime.provider;

import org.uberfire.provisioning.runtime.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.providers.ProviderType;
import org.uberfire.provisioning.runtime.providers.base.BaseProvider;

public class DockerProvider extends BaseProvider {

    
    

    public DockerProvider() {
    }
    
    public DockerProvider( ProviderConfiguration config ) {
        this( config, new DockerProviderType() );
    }

    public DockerProvider( ProviderConfiguration config,
                           ProviderType type ) {
        super( config.getName(), type );
        this.config = config;
        

    }

    

}
