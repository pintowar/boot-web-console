<script lang="ts">
  import consoleLogo from './assets/console.png'
  import CodeMirror from "svelte-codemirror-editor";
  import { groovy } from "./lib/groovy";
  import { oneDark } from "@codemirror/theme-one-dark";
  import { keymap } from "@codemirror/view"
  import { SyncLoader } from 'svelte-loading-spinners';
  
  interface ScriptResult {
    output: string[]
    result: string
  }

  let sample = ""

  let isEvaluating = false;
  let scriptBody = "";
  let evalResult: ScriptResult = null;

  const samplePath = (file: string) => {
    return `${import.meta.env.BASE_URL}/samples/${file}`
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
      fetch("/console/groovy", { method: "POST", body: data })
      .then(response => response.json())
      .then(response => evalResult = response)
      .finally(() => isEvaluating = false);
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
    <button id="send-button" type="button" on:click={remoteEval}>&#9654; Execute</button>
    
    <span class="pulled-right">
      <label for="input-code-example-select">Example:</label>
      <select id="input-code-example-select" bind:value={sample} on:change={sampleSelect}>
        <option value="" selected>---</option>
        <option value={samplePath("get-environment-info.groovy")}>Get environment info</option>
        <option value={samplePath("list-spring-beans.groovy")}>List all spring beans</option>
        <option value={samplePath("is-it-friday.groovy")}>Is it friday?</option>
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
        {#each evalResult.output as out}
          <div class="output">{out}</div>
        {/each}
      </div>
      {#if evalResult.result}
        <div class="result">
          Result = {evalResult.result}
        </div>
      {/if}
    {/if}
  </div>
</main>

