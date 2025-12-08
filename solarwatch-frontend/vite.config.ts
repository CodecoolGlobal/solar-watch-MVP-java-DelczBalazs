import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // Compose can inject these to route inside the Docker network
      // Fallbacks keep localhost for normal local dev
      '/api/ai': {
        target: process.env.VITE_DEV_PROXY_AI || 'http://localhost:8082',
        changeOrigin: true,
      },
      '/api': {
        target: process.env.VITE_DEV_PROXY_API || 'http://localhost:8080',
        changeOrigin: true,
      },
      '/ai': {
        target: process.env.VITE_DEV_PROXY_AI || 'http://localhost:8082',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/ai/, ''),
      },
    },
  },
})
