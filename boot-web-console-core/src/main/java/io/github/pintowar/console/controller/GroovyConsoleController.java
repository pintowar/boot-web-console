package io.github.pintowar.console.controller;

import io.github.pintowar.console.repl.Repl;
import io.github.pintowar.console.repl.ScriptResult;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static java.util.Collections.singletonMap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for evaluating scripts from groovy console.
 */
@Controller
@RequestMapping("/console")
public class GroovyConsoleController {

    private Map<String, Repl> repls;

    private ApplicationContext applicationContext;
    public GroovyConsoleController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.repls = Repl.getNamedRepls();
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

    @GetMapping(value = "/engines", produces="application/json")
    public ResponseEntity<Set<String>> engines() {
        Set<String> engines = repls.keySet();
        return ResponseEntity.ok(engines);
    }

    /**
     * Executes the given groovy script
     *
     * @param script the groovy script
     * @return the result object
     */
    @PostMapping(value = "/{engine}/eval", produces="application/json")
    @ResponseBody
    public CompletableFuture<ScriptResult> execute(@PathVariable String engine, @RequestParam String script) {
        Repl repl = repls.get(engine);
        return CompletableFuture
                .supplyAsync(() -> repl.execute(script, singletonMap("applicationContext", applicationContext)))
                .exceptionally(ScriptResult::create);
    }

}
