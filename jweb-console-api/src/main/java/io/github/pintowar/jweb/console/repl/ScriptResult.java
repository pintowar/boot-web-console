package io.github.pintowar.jweb.console.repl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public final class ScriptResult {

    private String[] stdout;
    private String[] stderr;
    private Object result;

    private ScriptResult() {
    }

    public ScriptResult(Object result, String[] stdout) {
        this.result = result;
        this.stdout = stdout;
    }

    public ScriptResult(String[] stderr) {
        this.stderr = stderr;
    }

    public String[] getStdout() {
        return stdout;
    }

    public String[] getStderr() {
        return stderr;
    }

    public Object getResult() {
        return result;
    }

    public static ScriptResult create(Throwable throwable) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            throwable.printStackTrace(new PrintStream(baos, true));
            String[] err = baos.toString().split(System.lineSeparator());
            return new ScriptResult(err);
        } catch (IOException e) {
            throw new IllegalStateException("Problem while closing script stderr buffer.", e);
        }
    }

    public static ScriptResult create(Object result, String output) {
        boolean hasLength = output != null && !output.isEmpty();
        return new ScriptResult(
                result != null ? result.toString() : null,
                hasLength ? output.split(System.lineSeparator()) : new String[]{}
        );
    }
}
