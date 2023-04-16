import type { ScriptResult } from './interfaces';

export async function engineEval(engine: string, data: FormData) {
    const resp = await fetch(`/console/${engine}/eval`, { method: "POST", body: data });
    const scriptResult = await resp.json() as ScriptResult;
    return scriptResult;
}

export async function listEngines() {
    const resp = await fetch(`/console/engines`);
    const engines = await resp.json() as string[];
    return engines;
}

export async function sampleSelect(sample: string) {
    if (sample) {
        const resp = await fetch(sample);
        return await resp.text();
    } else {
        return "";
    }
};