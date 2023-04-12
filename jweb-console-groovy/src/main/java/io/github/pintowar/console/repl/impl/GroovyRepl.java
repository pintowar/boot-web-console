package io.github.pintowar.console.repl.impl;

import io.github.pintowar.console.repl.BaseRepl;
import java.util.Map;

public class GroovyRepl extends BaseRepl {

    public GroovyRepl() {
        super();
    }

    public GroovyRepl(Map<String, Object> bindings) {
        super(bindings);
    }

    @Override
    public String getEngineName() {
        return "groovy";
    }

}
