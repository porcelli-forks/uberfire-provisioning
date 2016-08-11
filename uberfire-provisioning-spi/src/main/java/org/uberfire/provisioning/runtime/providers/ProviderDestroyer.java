package org.uberfire.provisioning.runtime.providers;

public interface ProviderDestroyer {

    boolean supports( final ProviderId providerId );

    void destroy( final ProviderId providerId );

}
