<script lang="ts">
    import type { ScriptResult } from '../lib/interfaces';
    import { onMount } from 'svelte';
    import CodeMirror from "svelte-codemirror-editor";
    import { oneDark } from "@codemirror/theme-one-dark";
    import { keymap } from "@codemirror/view"
  
    import { engineEval, listEngines, sampleSelect } from "../lib/services";
    import { langByEngine } from "../lib/langs";

    import consoleLogo from '../assets/console.png'

    const NO_ENGINE = "no-engine";

    const keymaps = keymap.of([
        { key: "Alt-Enter", run: () => {
            remoteEval();
            return true;
        }},
        { key: "Alt-r", run: () => {
            remoteEval();
            return true;
        }},
        { key: "Alt-w", run: () => {
            evalResult = null;
            return true;
        }},
    ]);

    export let isEvaluating;
    export let evalResult: ScriptResult;

    let sample = "";
    let engines: string[] = [];
    let selectedEngine: string = NO_ENGINE;
    let scriptBody = "";

    onMount(async () => {
        const resp = await listEngines();
        engines = resp.length > 0 ? resp : [NO_ENGINE];
        if (engines.length >= 1) {
            selectedEngine = engines[0];
        }
    })

    function cleanScript() {
        scriptBody = "";
        sample = "";
    }

    function samplePath(file: string) {
        const basePath = import.meta.env.DEV ? "" : import.meta.env.BASE_URL;
        return `${basePath}/samples/${file}`;
    }

    function listSamples(engine: string) {
        const samples = {
            "get-environment-info" : "Get environment info",
            "list-spring-beans" : "List all spring beans",
            "is-it-friday" : "Is it friday?"
        }
        return Object.keys(samples).map(key => ({
            value: samplePath(`${key}.${engine}`), desc: samples[key]
        }));
    }

    async function sampleToScript(){
        scriptBody = await sampleSelect(sample);  
    } 

    async function remoteEval() {
        evalResult = null;
        if (scriptBody) {
            const data = new FormData();
            data.append('script', scriptBody);

            try {
                isEvaluating = true;
                evalResult = await engineEval(selectedEngine, data);
            } finally {
                isEvaluating = false;
            }
        }
    }
</script>

<div>
    <div class="input-code-title">
        <img src={consoleLogo} alt="console-logo" />
        <span class="title">Edit code</span>
        <span class="engine">Engine:</span>
        {#if engines.length > 1}
            <select id="engine-select" bind:value={selectedEngine} on:change={cleanScript}>
                {#each engines as engine}
                    <option value={engine}>{engine}</option>
                {/each}
            </select>
        {:else}
            <span>{selectedEngine}</span>
        {/if}
        <button id="send-button" type="button" on:click={remoteEval}>&#9654; Execute</button>
        
        <span class="pulled-right">
            <label for="input-code-example-select">Example:</label>
            <select id="input-code-example-select" bind:value={sample} on:change={sampleToScript}>
                <option value="" selected>---</option>
                {#each listSamples(selectedEngine) as sample}
                    <option value={sample.value}>{sample.desc}</option>
                {/each}
            </select>
        </span>
    </div>
    {#if selectedEngine && selectedEngine != NO_ENGINE}
        <CodeMirror 
            bind:value={scriptBody}
            lang={langByEngine(selectedEngine)}
            theme={oneDark}
            extensions={[keymaps]}
            styles={{
            "&": {
                width: "100%",
                height: "300px",
            },
            }}
        />
    {/if}
</div>