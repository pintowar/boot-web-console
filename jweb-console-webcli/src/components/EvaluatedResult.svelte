<script lang="ts">
  import { SyncLoader } from "svelte-loading-spinners";
  import type { ScriptResult } from "../lib/interfaces";

  export let evalResult: Promise<ScriptResult>;

  function consoleOutput(buffer: string[]) {
    return buffer.join("\n");
  }
</script>

<div>
  <div class="result-code-title">
    <span class="title">Result</span>
  </div>
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
