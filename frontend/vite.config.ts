import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  build: {
    chunkSizeWarningLimit: 1000000,
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
