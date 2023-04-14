package io.github.pintowar.console.micronaut;

import io.micronaut.context.ExecutionHandleLocator;
import io.micronaut.context.annotation.Requires;
import io.micronaut.web.router.DefaultRouteBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Requires(property="jweb.console.enabled", value="true", defaultValue="false")
@Singleton
public class EvalConsoleConfiguration extends DefaultRouteBuilder {

    public EvalConsoleConfiguration(ExecutionHandleLocator executionHandleLocator, UriNamingStrategy uriNamingStrategy) {
        super(executionHandleLocator, uriNamingStrategy);
    }

    @Inject
    void consoleIndex(EvalConsoleHandler handler) {
        GET("/console", handler, "index");
    }

    @Inject
    void consoleEngines(EvalConsoleHandler handler) {
        GET("/console/engines", handler, "engines");
    }

    @Inject
    void consoleEval(EvalConsoleHandler handler) {
        POST("/console/{engine}/eval", handler, "execute", String.class, String.class);
    }

}
