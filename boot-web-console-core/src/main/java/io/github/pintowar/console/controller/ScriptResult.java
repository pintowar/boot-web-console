package io.github.pintowar.console.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import groovy.lang.GString;
import org.springframework.util.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
final class ScriptResult {

    private String[] output;
    private Object result;

    private ScriptResult() {
    }

    public String[] getOutput() {
        return output;
    }

    public Object getResult() {
        return result;
    }

    static ScriptResult create(Throwable throwable) {
        String message = throwable.getMessage() == null ? throwable.getClass().getName() : throwable.getMessage();
        return create(null, message);
    }

    static ScriptResult create(Object result, String output) {
        ScriptResult scriptletResult = new ScriptResult();
        scriptletResult.result = result != null ? result.toString() : null;
        scriptletResult.output = StringUtils.hasLength(output) ? output.split(System.lineSeparator()) : new String[]{};
        return scriptletResult;
    }
}
