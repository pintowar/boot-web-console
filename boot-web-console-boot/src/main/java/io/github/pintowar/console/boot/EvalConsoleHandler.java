package io.github.pintowar.console.boot;

import io.github.pintowar.console.repl.Repl;
import io.github.pintowar.console.repl.ScriptResult;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static org.springframework.web.servlet.function.ServerResponse.ok;
import static org.springframework.web.servlet.function.ServerResponse.temporaryRedirect;

/**
 * Controller for evaluating scripts from web console.
 */
public class EvalConsoleHandler {

    private Map<String, Repl> repls;
    private ApplicationContext applicationContext;

    public EvalConsoleHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.repls = Repl.getNamedRepls();
    }

    public ServerResponse index(ServerRequest req) {
        return temporaryRedirect(URI.create("/console/index.html")).build();
    }


    public ServerResponse engines(ServerRequest req) {
        Set<String> engines = repls.keySet();
        return json().body(engines);
    }

    public ServerResponse execute(ServerRequest req) {
        Repl repl = repls.get(req.pathVariable("engine"));
        try {
            String script = req.param("script").orElse(null);
            ScriptResult result = repl.execute(script, singletonMap("applicationContext", applicationContext));
            return json().body(result);
        } catch (RuntimeException e) {
            return json().body(ScriptResult.create(e));
        }
    }

    private ServerResponse.BodyBuilder json() {
        return ok().contentType(MediaType.APPLICATION_JSON);
    }

}
