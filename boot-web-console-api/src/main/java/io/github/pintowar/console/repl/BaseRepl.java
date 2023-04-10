package io.github.pintowar.console.repl;

import javax.script.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseRepl implements Repl {

    private final ScriptEngineManager manager = new ScriptEngineManager();
    protected final Map<String, Object> defaultBindings;

    public BaseRepl() {
        this(Collections.emptyMap());
    }

    public BaseRepl(Map<String, Object> defaultBindings) {
        this.defaultBindings = Collections.unmodifiableMap(defaultBindings);
    }

    public ScriptResult execute(String script, Map<String, Object> bindings) {
        Map<String, Object> mergedBindings = Stream
                .concat(defaultBindings.entrySet().stream(), bindings.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        try (StringWriter writer = new StringWriter()) {
            ScriptEngine engine = manager.getEngineByName(getEngineName());

            ScriptContext context = engine.getContext();
            context.setWriter(writer);
            context.setBindings(new SimpleBindings(mergedBindings), ScriptContext.ENGINE_SCOPE);

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

    @Override
    public ScriptResult execute(String script) {
        return execute(script, Collections.emptyMap());
    }
}
