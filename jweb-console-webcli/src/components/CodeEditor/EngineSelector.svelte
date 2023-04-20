<script lang="ts">
  import { createEventDispatcher, onMount } from "svelte";
  import {} from "svelte";

  import { listEngines } from "../../lib/services";
  import Select from "../CodeEditor/Select.svelte";
  import { NO_ENGINE } from "./constants";

  export let selectedEngine: string;

  let engines: string[] = [];

  const dispatch = createEventDispatcher();

  onMount(async () => {
    const resp = await listEngines();
    engines = resp.length > 0 ? resp : [NO_ENGINE];
    if (engines.length >= 1) {
      selectedEngine = engines[0];
    }
  });

  function onChange() {
    dispatch("change");
  }
</script>

{#if engines.length > 1}
  <Select
    options={engines.map((it) => ({
      value: it,
      desc: it,
    }))}
    bind:value={selectedEngine}
    on:change={onChange}
  />
{:else}
  <span>{selectedEngine}</span>
{/if}
