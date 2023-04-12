import { LanguageSupport, StreamLanguage } from "@codemirror/language"

import { groovy as groovyParser } from "@codemirror/legacy-modes/mode/groovy";
import { ruby as rubyParser } from "@codemirror/legacy-modes/mode/ruby";

function groovy() {
    return new LanguageSupport(StreamLanguage.define(groovyParser))
}

function ruby() {
    return new LanguageSupport(StreamLanguage.define(rubyParser))
}

export function langByEngine(engine: string) {
    switch (engine) {
        case "groovy":
            return groovy();
        case "jruby":
            return ruby();
        default:
            throw new Error("Invalid language");
    }
}