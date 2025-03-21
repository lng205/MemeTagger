import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: (id) => {
          if (id.includes('element-plus') || id.includes('@element-plus/icons-vue')) {
            return 'element-plus';
          }
          
          if (id.includes('node_modules')) {
            return 'vendor';
          }
        }
      }
    }
  }
})
