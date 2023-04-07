package io.github.pintowar.console.repl.impl;

import io.github.pintowar.console.repl.ScriptResult;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    void shouldIncludeApplicationContextInBoundVariables() {
        ScriptResult result = repl.execute("applicationContext != null");
        assertEquals("true", result.getResult());
        Assertions.assertArrayEquals(Arrays.array(), result.getOutput());
    }

    @Test
    void shouldWrapExceptionWhenExceptionIsThrown() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                repl.execute("throw new RuntimeException('test')")
        );

        assertEquals("test", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2 <!> 3"
    })
    void shouldThrowException(String script) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                repl.execute(script)
        );

        assertTrue(thrown.getMessage().contains("startup failed:"));
    }

}