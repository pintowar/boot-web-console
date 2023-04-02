package io.github.pintowar.console.repl;

import org.assertj.core.util.Arrays;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GroovyReplTest {

    private GroovyRepl repl;

    @BeforeEach
    void setUp() {
        repl = new GroovyRepl(Collections.singletonMap("applicationContext", "some object"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldIncludeScriptReturnResultInTheResultObject() {
        ScriptResult result = repl.execute("2 + 3");

        assertEquals("5", result.getResult());
        assertArrayEquals(Arrays.array(), result.getOutput());
    }

    @Test
    void shouldIncludeScriptOutputInTheResultObject() {
        ScriptResult result = repl.execute("println 2 + 3");

        assertNull(result.getResult());
        assertArrayEquals(Arrays.array("5"), result.getOutput());
    }

    @Test
    void shouldIncludeBothOutputAndResultObjectInTheResult() {
        ScriptResult result = repl.execute("println('test'); 2 + 3");

        assertEquals("5", result.getResult());
        assertArrayEquals(Arrays.array("test"), result.getOutput());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenScriptIsMissing() {
        assertThrows(IllegalArgumentException.class, () ->
                repl.execute(null)
        );
    }

    @Test
    public void shouldIncludeApplicationContextInBoundVariables() {
        ScriptResult result = repl.execute("applicationContext != null");
        assertEquals("true", result.getResult());
        Assertions.assertArrayEquals(Arrays.array(), result.getOutput());
    }

    @Test
    public void shouldWrapExceptionWhenExceptionIsThrown() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                repl.execute("throw new RuntimeException('test')")
        );

        assertEquals("test", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUsingThreadMethods() {
        MultipleCompilationErrorsException thrown = assertThrows(MultipleCompilationErrorsException.class, () ->
                repl.execute("Thread.sleep(500); 2 + 3")
        );

        assertTrue(thrown.getMessage().startsWith("startup failed:"));
    }

    @Test
    void shouldThrowExceptionWhenUsingSystemMethods() {
        MultipleCompilationErrorsException thrown = assertThrows(MultipleCompilationErrorsException.class, () ->
                repl.execute("System.exit(0); 2 + 3")
        );

        assertTrue(thrown.getMessage().startsWith("startup failed:"));
    }

    @Test
    void shouldThrowExceptionWhenScriptCompilationFails() {
        MultipleCompilationErrorsException thrown = assertThrows(MultipleCompilationErrorsException.class, () ->
                repl.execute("2 <!> 3")
        );

        assertTrue(thrown.getMessage().startsWith("startup failed:"));
    }
}