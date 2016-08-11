package org.uberfire.provisioning.pipeline.execution;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.uberfire.provisioning.pipeline.BiFunctionConfigExecutor;
import org.uberfire.provisioning.pipeline.ConfigExecutor;
import org.uberfire.provisioning.pipeline.ContextAware;
import org.uberfire.provisioning.pipeline.FunctionConfigExecutor;
import org.uberfire.provisioning.pipeline.Input;
import org.uberfire.provisioning.pipeline.Pipeline;
import org.uberfire.provisioning.pipeline.Stage;

import static org.uberfire.provisioning.util.VariableInterpolation.*;

public class PipelineExecutor {

    private final Map<Class, ConfigExecutor> configExecutors = new HashMap<>();

    public PipelineExecutor( final Collection<ConfigExecutor> configExecutors ) {
        for ( final ConfigExecutor configExecutor : configExecutors ) {
            this.configExecutors.put( configExecutor.executeFor(), configExecutor );
        }
    }

    public <T> void execute( final Input input,
                             final Pipeline pipeline,
                             final Consumer<T> callback ) {
        final PipelineContext context = new PipelineContext( pipeline );
        context.start( input );
        context.pushCallback( callback );
        continuePipeline( context );
    }

    private void continuePipeline( final PipelineContext context ) {
        while ( !context.isFinished() ) {
            final Stage<Object, ?> stage = getCurrentStage( context );
            final Object newInput = pollOutput( context );

            try {
                stage.execute( newInput, output -> {
                    final ConfigExecutor executor = resolve( output.getClass() );
                    if ( output instanceof ContextAware ) {
                        ( (ContextAware) output ).setContext( Collections.unmodifiableMap( context.getValues() ) );
                    }
                    final Object newOutput = interpolate( context.getValues(), output );
                    context.getValues().put( executor.inputId(), newOutput );
                    if ( executor instanceof BiFunctionConfigExecutor ) {
                        final Optional result = (Optional) ( (BiFunctionConfigExecutor) executor ).apply( newInput, newOutput );
                        context.pushOutput( executor.outputId(), result.get() );
                    } else if ( executor instanceof FunctionConfigExecutor ) {
                        final Optional result = (Optional) ( (FunctionConfigExecutor) executor ).apply( newOutput );
                        context.pushOutput( executor.outputId(), result.get() );
                    }

                    continuePipeline( context );
                } );
            } catch ( final Throwable t ) {
                throw new RuntimeException( "An error occurred while executing the " + ( stage == null ? "null" : stage.getName() ) + " stage.", t );
            }
            return;
        }
        if ( context.isFinished() ) {
            final Object output = pollOutput( context );
            while ( context.hasCallbacks() ) {
                context.applyCallbackAndPop( output );
            }
        }
    }

    private ConfigExecutor resolve( final Class<?> clazz ) {
        final ConfigExecutor result = configExecutors.get( clazz );
        if ( result != null ) {
            return result;
        }
        for ( final Map.Entry<Class, ConfigExecutor> entry : configExecutors.entrySet() ) {
            if ( entry.getKey().isAssignableFrom( clazz ) ) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static Object pollOutput( final PipelineContext context ) {
        return context.pollOutput()
                .orElseThrow( () -> new IllegalStateException( "The " + PipelineContext.class.getSimpleName() + " was polled with no previous output." ) );
    }

    private static Stage<Object, ?> getCurrentStage( final PipelineContext context ) {
        return context
                .getCurrentStage()
                .orElseThrow( () -> new IllegalStateException( "There was not current stage even though the process has not finished." ) );
    }
}