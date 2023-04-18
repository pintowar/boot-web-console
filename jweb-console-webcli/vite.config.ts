/// <reference types="vitest" />

import { defineConfig } from "vite";
import { svelte } from "@sveltejs/vite-plugin-svelte";

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      "/console/": {
        target: "http://localhost:8080/",
        changeOrigin: true,
        secure: false,
      },
    },
  },
  test: {
    globals: true,
    environment: "jsdom",
    coverage: {
      provider: "istanbul",
      reporter: ["lcov", "html", "clover"],
    },
  },
  plugins: [svelte({ hot: !process.env.VITEST })],
  // optimizeDeps: {
  //   exclude: ["codemirror", "@codemirror/language-javascript" /* ... */],
  // }
});
