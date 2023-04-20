<div>
  <div class="header">
    <slot name="header" />
  </div>
  <div>
    <slot name="content" />
  </div>
</div>

<style>
  .header {
    background-color: #4d4d4d;
    border-top: 1px solid #000;
    border-bottom: 1px solid #000;
    font-family: sans-serif;
    font-size: 12px;
    color: #fff;
    padding: 6px;
  }
</style>
