package io.github.pintowar.console.repl;

import javax.script.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

public abstract class Repl {

    private final ScriptEngineManager manager = new ScriptEngineManager();
    protected final Map<String, Object> bindings;

    public Repl(Map<String, Object> bindings) {
        this.bindings = Collections.unmodifiableMap(bindings);
    }

    public ScriptResult executeJsr223(String engineName, String script) {
        try (StringWriter writer = new StringWriter()) {
            ScriptEngine engine = manager.getEngineByName(engineName);

            ScriptContext context = engine.getContext();
            context.setWriter(writer);
            context.setBindings(new SimpleBindings(bindings), ScriptContext.ENGINE_SCOPE);

            Object result = engine.eval(script);
            return ScriptResult.create(result, writer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            Throwable cause = e.getCause();
            Throwable topCause = cause != null ? cause.getCause() : null;
            throw new IllegalArgumentException(topCause != null ? topCause.getMessage() : e.getMessage(), e);
        }
    }

    public abstract ScriptResult execute(String script);

}
