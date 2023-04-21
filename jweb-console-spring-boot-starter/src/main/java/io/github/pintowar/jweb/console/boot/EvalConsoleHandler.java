package io.github.pintowar.jweb.console.boot;

import static java.util.Collections.singletonMap;
import static org.springframework.web.servlet.function.ServerResponse.ok;

import io.github.pintowar.jweb.console.repl.Repl;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/** Handler for evaluating scripts from web console. */
public class EvalConsoleHandler {

  private Map<String, Repl> repls;
  private ApplicationContext applicationContext;

  public EvalConsoleHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.repls = Repl.getNamedRepls();
  }

  public ServerResponse index() {
    return ok().render("/console/index.html");
  }

  public ServerResponse engines() {
    Set<String> engines = repls.keySet();
    return json().body(engines);
  }

  public ServerResponse execute(ServerRequest req) {
    Repl repl = repls.get(req.pathVariable("engine"));
    ScriptResult result =
        repls
            .get(req.pathVariable("engine"))
            .eval(
                req.param("script").orElse(null),
                singletonMap("applicationContext", applicationContext));
    return json().body(result);
  }

  private ServerResponse.BodyBuilder json() {
    return ok().contentType(MediaType.APPLICATION_JSON);
  }
}
