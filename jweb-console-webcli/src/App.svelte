<script lang="ts">
  import type { ScriptResult } from "./lib/interfaces";

  import EvaluatedResult from './components/EvaluatedResult.svelte';
  import CodeEditor from './components/CodeEditor/index.svelte';

  let isEvaluating = false;
  let evalResult: ScriptResult = null;
  
</script>

<main>
  <CodeEditor bind:evalResult={evalResult} bind:isEvaluating={isEvaluating}/>
  <EvaluatedResult evalResult={evalResult} isEvaluating={isEvaluating} />
</main>
