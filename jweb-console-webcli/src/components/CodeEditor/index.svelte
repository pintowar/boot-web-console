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
  import Card from "../Card.svelte";

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

  export let evalResult: Promise<ScriptResult>;

  let sample = "";
  let selectedEngine: string = NO_ENGINE;
  let scriptBody = "";

  function handleChangeEngine() {
    scriptBody = "";
    sample = "";
  }

  function remoteEval() {
    evalResult = Promise.resolve({ result: "", stdout: [], stderr: [] });
    if (scriptBody) {
      evalResult = engineEval(selectedEngine, scriptBody);
    }
  }
</script>

<Card>
  <div slot="header">
    <img src={consoleLogo} alt="console-logo" />
    <span class="title">Edit code</span>
    <span class="engine">Engine:</span>
    <EngineSelector bind:selectedEngine on:change={handleChangeEngine} />
    <button id="send-button" type="button" on:click={remoteEval}>&#9654; Execute</button>

    <div class="pulled-right">
      <SampleSelector bind:scriptBody {sample} {selectedEngine} />
    </div>
  </div>

  <div slot="content">
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
</Card>

<style>
  img {
    position: absolute;
    top: 1px;
  }

  .title {
    margin-left: 40px;
  }

  .engine {
    margin-left: 20px;
  }

  button {
    background-color: #5f5f5f;
    font-family: sans-serif;
    font-size: 12px;
    height: 22px;
    color: #fff;
    border: 1px solid #ddd;
    margin-left: 20px;
  }

  .pulled-right {
    float: right;
  }
</style>
