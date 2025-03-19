<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Auth from './components/Auth.vue';
import MemeUploader from './components/MemeUploader.vue';

const isAuthenticated = ref(false);

onMounted(() => {
  // Check if token exists in localStorage
  isAuthenticated.value = !!localStorage.getItem('auth_token');
  
  // Listen for storage events (if user logs in/out in another tab)
  window.addEventListener('storage', (event) => {
    if (event.key === 'auth_token') {
      isAuthenticated.value = !!event.newValue;
    }
  });
});

const updateAuthState = (state: boolean) => {
  isAuthenticated.value = state;
};
</script>

<template>
  <el-container direction="vertical" class="app">
    <el-header class="app-header">
      <h1>Meme Tagger</h1>
      <p>Upload and share your favorite memes</p>
    </el-header>
    
    <el-main>
      <Auth @login-success="updateAuthState(true)" @logout="updateAuthState(false)" />
      
      <MemeUploader v-if="isAuthenticated" class="content" />
    </el-main>
    
    <el-footer class="app-footer">
      <p>&copy; {{ new Date().getFullYear() }} Meme Tagger</p>
    </el-footer>
  </el-container>
</template>

<style>
body {
  margin: 0;
  padding: 0;
  min-height: 100vh;
  font-family: var(--el-font-family);
  background-color: var(--el-bg-color);
  color: var(--el-text-color-primary);
}

.app {
  min-height: 100vh;
}

.app-header {
  background-color: var(--el-color-primary);
  color: white;
  text-align: center;
  padding: 20px !important;
  height: auto !important;
}

.app-header h1 {
  margin: 0;
  font-size: 2em;
}

.app-header p {
  margin: 10px 0 0;
  opacity: 0.8;
}

.content {
  max-width: 600px;
  margin: 30px auto 0;
}

.app-footer {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary-dark-2);
  text-align: center;
  padding: 15px !important;
  height: auto !important;
  margin-top: auto;
}
</style>
