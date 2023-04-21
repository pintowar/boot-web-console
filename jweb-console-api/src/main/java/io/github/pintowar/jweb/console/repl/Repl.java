package io.github.pintowar.jweb.console.repl;

import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public interface Repl {

  static Map<String, Repl> getNamedRepls() {
    Map<String, Repl> repls = new HashMap<>();
    ServiceLoader.load(Repl.class)
        .iterator()
        .forEachRemaining(it -> repls.put(it.getEngineName(), it));
    return repls;
  }

  ScriptResult execute(String script, Map<String, Object> bindings);

  default ScriptResult execute(String script) {
    return execute(script, emptyMap());
  }

  default ScriptResult eval(String script) {
    return eval(script, emptyMap());
  }

  default ScriptResult eval(String script, Map<String, Object> bindings) {
    try {
      return execute(script, bindings);
    } catch (RuntimeException e) {
      return ScriptResult.create(e);
    }
  }

  String getEngineName();
}
