package io.github.pintowar.console.micronaut;

import io.github.pintowar.console.repl.Repl;
import io.github.pintowar.console.repl.ScriptResult;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.io.IOUtils;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonMap;

@Singleton
public class EvalConsoleHandler {

    private Map<String, Repl> repls;
    private ApplicationContext applicationContext;

    private ResourceResolver resourceResolver;

    public EvalConsoleHandler(ApplicationContext applicationContext, ResourceResolver resourceResolver) {
        this.repls = Repl.getNamedRepls();
        this.applicationContext = applicationContext;
        this.resourceResolver = resourceResolver;
    }

    @Get
    @Produces(MediaType.TEXT_HTML)
    public Optional<String> index() {
        return resourceResolver
                .getResourceAsStream("classpath:public/console/index.html")
                .map(ins -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(ins))) {
                        return IOUtils.readText(reader);
                    } catch (IOException e) {
                        return "";
                    }
                });
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> engines() {
        return repls.keySet();
    }

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public ScriptResult execute(@PathVariable String engine, @QueryValue String script) {
        Repl repl = repls.get(engine);
        try {
            return repl.execute(script, singletonMap("applicationContext", applicationContext));
        } catch (RuntimeException e) {
            return ScriptResult.create(e);
        }
    }
}
