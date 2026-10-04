import tailwindcss from '@tailwindcss/vite'
import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

export default defineConfig({
  // '/' serves from a domain root; set BASE=/repo-name/ to serve from a subpath (GitHub Pages).
  base: process.env.BASE ?? '/',
  plugins: [vue(), tailwindcss()],
  // The docs are the repository's own Markdown, one level above the landing.
  server: { fs: { allow: ['..'] } },
})
