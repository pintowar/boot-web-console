<script lang="ts">
  import { sampleSelect } from "../../lib/services";

  export let scriptBody: string;

  export let sample: string;
  export let selectedEngine: string;

  function samplePath(file: string) {
    const basePath = import.meta.env.DEV ? "" : import.meta.env.BASE_URL;
    return `${basePath}/samples/${file}`;
  }

  function listSamples(engine: string) {
    const samples = {
      "get-environment-info": "Get environment info",
      "list-spring-beans": "List all spring beans",
      "is-it-friday": "Is it friday?",
    };
    return Object.keys(samples).map((key) => ({
      value: samplePath(`${key}.${engine}`),
      desc: samples[key] as string,
    }));
  }

  async function sampleToScript() {
    scriptBody = await sampleSelect(sample);
  }
</script>

<span class="pulled-right">
  <label for="input-code-example-select">Example:</label>
  <select id="input-code-example-select" bind:value={sample} on:change={sampleToScript}>
    <option value="" selected>---</option>
    {#each listSamples(selectedEngine) as sample}
      <option value={sample.value}>{sample.desc}</option>
    {/each}
  </select>
</span>
