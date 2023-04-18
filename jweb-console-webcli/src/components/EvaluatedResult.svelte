<script lang="ts">
  import { SyncLoader } from "svelte-loading-spinners";
  import type { ScriptResult } from "../lib/interfaces";

  export let isEvaluating: boolean;
  export let evalResult: ScriptResult;
</script>

<div>
  <div class="result-code-title">
    <span class="title">Result</span>
  </div>
  <div class="result-output">
    {#if isEvaluating}
      <div class="loading">
        <SyncLoader size="60" color="white" unit="px" duration="1s" />
      </div>
    {/if}
    {#if !isEvaluating && evalResult}
      <div>
        {#if evalResult?.stdout}
          <pre class="stdout">{evalResult.stdout.join("\n")}</pre>
        {/if}
      </div>
      <div>
        {#if evalResult?.stderr}
          <pre class="stderr">{evalResult.stderr.join("\n")}</pre>
        {/if}
      </div>
      {#if evalResult.result}
        <div class="result">
          Result: <pre>{evalResult.result}</pre>
        </div>
      {/if}
    {/if}
  </div>
</div>
