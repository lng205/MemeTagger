<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Auth from './components/Auth.vue';
import MemeUploader from './components/MemeUploader.vue';

const isAuthenticated = ref(false);

// Check auth state on mount and listen for changes
onMounted(() => {
  isAuthenticated.value = !!localStorage.getItem('auth_token');
  
  window.addEventListener('storage', (event) => {
    if (event.key === 'auth_token') isAuthenticated.value = !!event.newValue;
  });
});
</script>

<template>
  <el-container direction="vertical" class="app">
    <el-header class="header">
      <h1>Meme Tagger</h1>
      <p>Upload and share your favorite memes</p>
    </el-header>
    
    <el-main>
      <Auth @login-success="isAuthenticated = true" @logout="isAuthenticated = false" />
      <MemeUploader v-if="isAuthenticated" class="content" />
    </el-main>
    
    <el-footer class="footer">
      &copy; {{ new Date().getFullYear() }} Meme Tagger
    </el-footer>
  </el-container>
</template>

<style>
body {
  margin: 0;
  font-family: var(--el-font-family);
  background-color: var(--el-bg-color);
  color: var(--el-text-color-primary);
}

.app { min-height: 100vh; }

.header {
  background-color: var(--el-color-primary);
  color: white;
  text-align: center;
  padding: 20px !important;
  height: auto !important;
}

.header h1 { 
  margin: 0;
  font-size: 2em;
}

.header p { 
  margin: 10px 0 0;
  opacity: 0.8;
}

.content {
  max-width: 600px;
  margin: 30px auto 0;
}

.footer {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary-dark-2);
  text-align: center;
  padding: 15px !important;
  height: auto !important;
  margin-top: auto;
}
</style>
