<script lang="ts">
  import { SyncLoader } from "svelte-loading-spinners";
  import type { ScriptResult } from "../lib/interfaces";
  import Card from "./Card.svelte";

  export let evalResult: Promise<ScriptResult>;

  function consoleOutput(buffer: string[]) {
    return buffer.join("\n");
  }
</script>

<Card>
  <div slot="header">
    <span class="title">Result</span>
  </div>

  <div slot="content">
    <div>
      <div class="result-output">
        {#await evalResult}
          <div class="loading">
            <SyncLoader size="60" color="white" unit="px" duration="1s" />
          </div>
        {:then evaluated}
          <div>
            {#if evaluated.stdout}
              <pre class="stdout">{consoleOutput(evaluated.stdout)}</pre>
            {/if}
          </div>
          <div>
            {#if evaluated.stderr}
              <pre class="stderr">{consoleOutput(evaluated.stderr)}</pre>
            {/if}
          </div>
          {#if evaluated.result}
            <div class="result">
              Result: <pre>{evaluated.result}</pre>
            </div>
          {/if}
        {:catch}
          <div class="loading">Error!</div>
        {/await}
      </div>
    </div>
  </div>
</Card>

<style>
  .result-output {
    height: calc(100vh - 364px);
    color: white;
    background-color: #282c34;
    overflow-y: auto;
  }

  .result-output .loading {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .result-output .result {
    color: #528bff;
  }

  .result-output .stdout {
    color: #abb2bf;
  }

  .result-output .stderr {
    color: red;
  }
</style>
