package io.github.pintowar.jweb.console.repl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ReplTest {

  @Test
  void shouldGetNamedRepls() {
    Map<String, Repl> repls = Repl.getNamedRepls();

    assertEquals(1, repls.size());
    assertTrue(repls.containsKey("mock"));
  }

  @Test
  void shouldEvaluateSuccess() {
    Repl repl = Repl.getNamedRepls().get("mock");
    ScriptResult result = repl.eval("println('test'); println('test'); 1 + 2");

    assertEquals("3", result.getResult());
    assertArrayEquals(new String[] {"test", "test"}, result.getStdout());
    assertNull(result.getStderr());
  }

  @Test
  void shouldEvaluateFail() {
    Repl repl = Repl.getNamedRepls().get("mock");
    ScriptResult result = repl.eval("1 / 0", Collections.singletonMap("some", "error"));

    assertNull(result.getResult());
    assertNull(result.getStdout());

    assertEquals("java.lang.ArithmeticException: Division by zero", result.getStderr()[0]);
    assertTrue(result.getStderr().length > 20);
  }
}
