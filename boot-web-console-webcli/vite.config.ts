import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
        "/console/groovy": {
            target: "http://localhost:8080/",
            changeOrigin: true,
            secure: false,
        },
    },
  },
  plugins: [svelte()],
  // optimizeDeps: {
  //   exclude: ["codemirror", "@codemirror/language-javascript" /* ... */],
  // }
})
