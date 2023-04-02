import {LanguageSupport, StreamLanguage} from "@codemirror/language"

import { groovy as groovyParser } from "@codemirror/legacy-modes/mode/groovy";

export function groovy() {
    return new LanguageSupport(StreamLanguage.define(groovyParser))
}