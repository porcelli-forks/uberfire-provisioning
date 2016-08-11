package org.uberfire.provisioning.source.git.executor;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import javax.inject.Inject;

import org.uberfire.java.nio.file.FileSystems;
import org.uberfire.provisioning.config.Config;
import org.uberfire.provisioning.pipeline.FunctionConfigExecutor;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.source.git.GitRepository;
import org.uberfire.provisioning.source.git.UFLocal;
import org.uberfire.provisioning.source.git.config.GitConfig;

import static org.uberfire.commons.validation.PortablePreconditions.*;

public class GitConfigExecutor implements FunctionConfigExecutor<GitConfig, Source> {

    private final SourceRegistry sourceRegistry;

    @Inject
    public GitConfigExecutor( final SourceRegistry sourceRegistry ) {
        this.sourceRegistry = sourceRegistry;
    }

    @Override
    public Optional<Source> apply( final GitConfig gitConfig ) {
        checkNotEmpty( "repo-name parameter is mandatory", gitConfig.getRepoName() );

        final URI uri = URI.create( "git://" + gitConfig.getRepoName() );
        FileSystems.newFileSystem( uri, new HashMap<String, Object>() {{
            if ( gitConfig.getOrigin() != null && !gitConfig.getOrigin().isEmpty() ) {
                put( "origin", gitConfig.getOrigin() );
            } else {
                put( "init", Boolean.TRUE );
            }
            if ( gitConfig.getOutPath() != null && !gitConfig.getOutPath().isEmpty() ) {
                put( "out-dir", gitConfig.getOutPath() );
            }
        }} );

        final GitRepository gitRepository = (GitRepository) new UFLocal().getRepository( gitConfig.getRepoName(), Collections.emptyMap() );
        final Optional<Source> source = Optional.ofNullable( gitRepository.getSource( gitConfig.getBranch() != null && !gitConfig.getBranch().isEmpty() ? gitConfig.getBranch() : "master" ) );
        if ( source.isPresent() ) {
            sourceRegistry.registerSource( gitRepository, source.get() );
        }
        return source;
    }

    @Override
    public Class<? extends Config> executeFor() {
        return GitConfig.class;
    }

    @Override
    public String outputId() {
        return "source";
    }

    @Override
    public String inputId() {
        return "git-config";
    }
}
