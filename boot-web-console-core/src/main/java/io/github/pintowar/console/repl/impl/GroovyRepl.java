package io.github.pintowar.console.repl.impl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.transform.TimedInterrupt;
import io.github.pintowar.console.repl.Repl;
import io.github.pintowar.console.repl.ScriptResult;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonMap;

public class GroovyRepl extends Repl {

    private static final long SCRIPT_TIMEOUT_IN_SECONDS = 5;
    private static final List<String> RECEIVERS_BLACK_LIST = Stream.of(System.class, Thread.class)
            .map(Class::getName)
            .collect(Collectors.toList());

    public GroovyRepl(Map<String, Object> bindings) {
        super(bindings);
    }

    public ScriptResult execute(String script) {
//        return groovyShellExecute(script);
        return executeJsr223("groovy", script);
    }

    private ScriptResult groovyShellExecute(String script) {
        try (StringWriter writer = new StringWriter()) {
            GroovyShell groovyShell = createGroovyShell(writer);
            Object result = groovyShell.evaluate(script);
            return ScriptResult.create(result, writer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MultipleCompilationErrorsException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private GroovyShell createGroovyShell(Writer writer) {
        CompilerConfiguration configuration = createCompilerConfiguration();
        Binding binding = createBinding(writer);
        return new GroovyShell(binding, configuration);
    }

    private Binding createBinding(Writer writer) {
        Binding binding = new Binding();
        bindings.forEach((k, v) -> {
            if (!k.equals("out")) {
                binding.setVariable(k, v);
            }
        });

        binding.setProperty("out", writer);
        return binding;
    }

    private CompilerConfiguration createCompilerConfiguration() {
        ASTTransformationCustomizer timedCustomizer = new ASTTransformationCustomizer(
                singletonMap("value", SCRIPT_TIMEOUT_IN_SECONDS), TimedInterrupt.class
        );
        SecureASTCustomizer secureCustomizer = new SecureASTCustomizer();
        secureCustomizer.setDisallowedReceivers(RECEIVERS_BLACK_LIST);
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(secureCustomizer, timedCustomizer);
        return configuration;
    }
}
