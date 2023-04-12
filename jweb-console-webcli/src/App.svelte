<script lang="ts">
  import { onMount } from 'svelte';
  import consoleLogo from './assets/console.png'
  import CodeMirror from "svelte-codemirror-editor";
    
  import { oneDark } from "@codemirror/theme-one-dark";
  import { keymap } from "@codemirror/view"
  import { SyncLoader } from 'svelte-loading-spinners';

  import { groovy } from "./lib/langs";
  import { engineEval, listEngines } from "./lib/services";
  import type { ScriptResult } from "./lib/interfaces";  
  

  const NO_ENGINE = "no-engine";

  let sample = ""
  let engines: string[] = []
  let engine: string = NO_ENGINE

  let isEvaluating = false;
  let scriptBody = "";
  let evalResult: ScriptResult = null;

  onMount(async () => {
    await listEngines(resp => engines = resp.length > 0 ? resp : [NO_ENGINE]);
    if (engines.length == 1) {
      engine = engines[0];
    }
  })

  const samplePath = (file: string) => {
    const basePath = import.meta.env.DEV ? "" : import.meta.env.BASE_URL;
    return `${basePath}/samples/${file}`;
  }

  const listSamples = (engine: string) => {
    const samples = {
      "get-environment-info" : "Get environment info",
      "list-spring-beans" : "List all spring beans",
      "is-it-friday" : "Is it friday?"
    }
    return Object.keys(samples).map(key => ({
      value: samplePath(`${key}.${engine}`), desc: samples[key]
    }));
  }

  const sampleSelect = () => {
    sample && fetch(sample)
    .then(response => response.text())
    .then(response => scriptBody = response);
  };

  const remoteEval = () => {
    evalResult = null;
    if (scriptBody) {
      const data = new FormData();
      data.append('script', scriptBody);

      isEvaluating = true;
      engineEval({
        engine,
        data,
        onSuccess: (response) => evalResult = response,
        onFinally: () => isEvaluating = false
      })
    }
  };

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
</script>

<main>
  <div class="input-code-title">
    
    <img src={consoleLogo} alt="console-logo" />
    <span class="title">Edit code</span>
    <span class="engine">Engine: {engine}</span>
    <button id="send-button" type="button" on:click={remoteEval}>&#9654; Execute</button>
    
    <span class="pulled-right">
      <label for="input-code-example-select">Example:</label>
      <select id="input-code-example-select" bind:value={sample} on:change={sampleSelect}>
        <option value="" selected>---</option>
        {#each listSamples(engine) as sample}
          <option value={sample.value}>{sample.desc}</option>
        {/each}
      </select>
    </span>
  </div>
  <CodeMirror 
    bind:value={scriptBody}
    lang={groovy()}
    theme={oneDark}
    extensions={[keymaps]}
    styles={{
      "&": {
        width: "100%",
        height: "300px",
      },
    }}
  />
  <div class="result-code-title"><span class="title">Result</span></div>
  <div class="result-output">
    {#if isEvaluating}
      <div class="loading">
        <SyncLoader size="60" color="white" unit="px" duration="1s" />
      </div>
    {/if}
    {#if !isEvaluating && evalResult}
      <div>
        {#if evalResult?.stdout}
          <pre class="stdout">{evalResult.stdout.join('\n')}</pre>
        {/if}
      </div>
      <div>
        {#if evalResult?.stderr}
          <pre class="stderr">{evalResult.stderr.join('\n')}</pre>
        {/if}
      </div>
      {#if evalResult.result}
        <div class="result">
          Result: <pre>{evalResult.result}</pre>
        </div>
      {/if}
    {/if}
  </div>
</main>

