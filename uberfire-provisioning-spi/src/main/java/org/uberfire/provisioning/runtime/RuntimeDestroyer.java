package org.uberfire.provisioning.runtime;

public interface RuntimeDestroyer {

    boolean supports( final RuntimeId runtimeId );

    void destroy( final RuntimeId runtimeId );

}
