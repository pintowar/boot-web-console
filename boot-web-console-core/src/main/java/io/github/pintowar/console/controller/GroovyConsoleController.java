package io.github.pintowar.console.controller;

import io.github.pintowar.console.repl.Repl;
import io.github.pintowar.console.repl.ScriptResult;
import io.github.pintowar.console.repl.impl.GroovyRepl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for evaluating scripts from groovy console.
 */
@Controller
@RequestMapping("/console")
public class GroovyConsoleController {

    private final Repl repl;

    public GroovyConsoleController(ApplicationContext applicationContext) {
        this.repl = new GroovyRepl(Collections.singletonMap("applicationContext", applicationContext));
    }

    /**
     * Redirects to groovy console index page.
     *
     * @return the redirect view of groovy console index page
     */
    @GetMapping
    public String index() {
        return "redirect:/console/index.html";
    }

    /**
     * Executes the given groovy script
     *
     * @param script the groovy script
     * @return the result object
     */
    @PostMapping(value = "/groovy")
    @ResponseBody
    public CompletableFuture<ScriptResult> execute(@RequestParam String script) {
        return CompletableFuture
                .supplyAsync(() -> repl.execute(script))
                .exceptionally(ScriptResult::create);
    }

}
