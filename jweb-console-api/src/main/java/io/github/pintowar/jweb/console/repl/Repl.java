package io.github.pintowar.jweb.console.repl;

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

  ScriptResult execute(String script);

  ScriptResult execute(String script, Map<String, Object> bindings);

  String getEngineName();
}
