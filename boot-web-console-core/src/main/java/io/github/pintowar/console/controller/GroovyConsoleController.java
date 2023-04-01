package io.github.pintowar.console.controller;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.transform.TimedInterrupt;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonMap;

/**
 * Controller for evaluating scripts from groovy console.
 */
@Controller
@RequestMapping("/console")
public class GroovyConsoleController {

    private static final long SCRIPT_TIMEOUT_IN_SECONDS = 5;
    private static final List<String> RECEIVERS_BLACK_LIST = Stream.of(System.class, Thread.class)
            .map(Class::getName)
            .collect(Collectors.toList());

    private ApplicationContext applicationContext;

    public GroovyConsoleController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Redirects to groovy console index page.
     *
     * @return the redirect view of groovy console index page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "redirect:/console/index.html";
    }

    /**
     * Executes the given groovy script
     *
     * @param script the groovy script
     * @return the result object
     */
    @RequestMapping(value = "/groovy", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public CompletableFuture<ScriptResult> execute(@RequestParam String script) {
        return CompletableFuture.supplyAsync(() -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GroovyShell groovyShell = createGroovyShell(out);
            Object result = groovyShell.evaluate(script);
            return ScriptResult.create(result.toString(), out.toString());
        }).exceptionally(ScriptResult::create);
    }

    private GroovyShell createGroovyShell(OutputStream outputStream) {
        CompilerConfiguration configuration = createCompilerConfiguration();
        Binding binding = createBinding(outputStream);
        return new GroovyShell(binding, configuration);
    }

    private Binding createBinding(OutputStream outputStream) {
        Binding binding = new Binding();
        binding.setVariable("applicationContext", applicationContext);
        binding.setProperty("out", new PrintStream(outputStream, true));
        return binding;
    }

    private CompilerConfiguration createCompilerConfiguration() {
        ASTTransformationCustomizer timedCustomizer = new ASTTransformationCustomizer(singletonMap("value", SCRIPT_TIMEOUT_IN_SECONDS), TimedInterrupt.class);
        SecureASTCustomizer secureCustomizer = new SecureASTCustomizer();
        secureCustomizer.setReceiversBlackList(RECEIVERS_BLACK_LIST);
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(secureCustomizer, timedCustomizer);
        return configuration;
    }

}
