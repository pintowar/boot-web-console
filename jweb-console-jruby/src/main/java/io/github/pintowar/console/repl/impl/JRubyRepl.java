package io.github.pintowar.console.repl.impl;

import io.github.pintowar.console.repl.ReplJsr223;
import io.github.pintowar.console.repl.ScriptResult;

import java.util.Map;

public class JRubyRepl extends ReplJsr223 {

    public JRubyRepl() {
        super();
    }

    public JRubyRepl(Map<String, Object> bindings) {
        super(bindings);
    }

    @Override
    public String getEngineName() {
        return "jruby";
    }

    @Override
    public ScriptResult execute(String script) {
        return super.execute(script);
    }
}
