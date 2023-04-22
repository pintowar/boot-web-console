<script lang="ts">
  import type { ScriptResult } from "./lib/interfaces";

  import EvaluatedResult from "./components/EvaluatedResult.svelte";
  import CodeEditor from "./components/CodeEditor/index.svelte";

  let evalResult: Promise<ScriptResult> = Promise.resolve({ result: "", stdout: [], stderr: [] });
</script>

<main>
  <CodeEditor bind:evalResult />
  <EvaluatedResult {evalResult} />
</main>
