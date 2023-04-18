<script lang="ts">
  import type { ScriptResult } from "../../lib/interfaces";
  import CodeMirror from "svelte-codemirror-editor";
  import { oneDark } from "@codemirror/theme-one-dark";
  import { keymap } from "@codemirror/view";

  import EngineSelector from "./EngineSelector.svelte";
  import SampleSelector from "./SampleSelector.svelte";
  import { NO_ENGINE } from "./constants";
  import { engineEval } from "../../lib/services";
  import { langByEngine } from "../../lib/langs";

  import consoleLogo from "../../assets/console.png";

  const keymaps = keymap.of([
    {
      key: "Alt-Enter",
      run: () => {
        remoteEval();
        return true;
      },
    },
    {
      key: "Alt-r",
      run: () => {
        remoteEval();
        return true;
      },
    },
    {
      key: "Alt-w",
      run: () => {
        evalResult = null;
        return true;
      },
    },
  ]);

  export let isEvaluating;
  export let evalResult: ScriptResult;

  let sample = "";
  let selectedEngine: string = NO_ENGINE;
  let scriptBody = "";

  function handleChangeEngine() {
    scriptBody = "";
    sample = "";
  }

  async function remoteEval() {
    evalResult = null;
    if (scriptBody) {
      try {
        isEvaluating = true;
        evalResult = await engineEval(selectedEngine, scriptBody);
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
    <EngineSelector bind:selectedEngine on:change={handleChangeEngine} />
    <button id="send-button" type="button" on:click={remoteEval}>&#9654; Execute</button>

    <SampleSelector bind:scriptBody {sample} {selectedEngine} />
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
