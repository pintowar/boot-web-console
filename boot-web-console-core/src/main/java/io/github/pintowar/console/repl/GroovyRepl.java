package io.github.pintowar.console.repl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.transform.TimedInterrupt;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonMap;

public class GroovyRepl {

    private static final long SCRIPT_TIMEOUT_IN_SECONDS = 5;
    private static final List<String> RECEIVERS_BLACK_LIST = Stream.of(System.class, Thread.class)
            .map(Class::getName)
            .collect(Collectors.toList());
    private final Map<String, Object> bindings;

    public GroovyRepl(Map<String, Object> bindings) {
        this.bindings = Collections.unmodifiableMap(bindings);
    }

    public ScriptResult execute(String script) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GroovyShell groovyShell = createGroovyShell(out);
        Object result = groovyShell.evaluate(script);
        return ScriptResult.create(result, out.toString());
    }

    private GroovyShell createGroovyShell(OutputStream outputStream) {
        CompilerConfiguration configuration = createCompilerConfiguration();
        Binding binding = createBinding(outputStream);
        return new GroovyShell(binding, configuration);
    }

    private Binding createBinding(OutputStream outputStream) {
        Binding binding = new Binding();
        bindings.forEach((k, v) -> {
            if (!k.equals("out")) {
                binding.setVariable(k, v);
            }
        });

        binding.setProperty("out", new PrintStream(outputStream, true));
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
