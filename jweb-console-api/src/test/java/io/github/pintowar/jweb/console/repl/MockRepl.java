package io.github.pintowar.jweb.console.repl;

import java.util.Map;

public class MockRepl implements Repl {

  @Override
  public ScriptResult execute(String script) {
    return ScriptResult.create(3, "test\ntest");
  }

  @Override
  public ScriptResult execute(String script, Map<String, Object> bindings) {
    if (bindings.size() == 0) {
      return execute(script);
    } else {
      throw new ArithmeticException("Division by zero");
    }
  }

  @Override
  public String getEngineName() {
    return "mock";
  }
}
