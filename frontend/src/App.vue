<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Auth from './components/Auth.vue';
import MemeUploader from './components/MemeUploader.vue';
import ApiKeySettings from './components/ApiKeySettings.vue';
import UserAccount from './components/UserAccount.vue';
import settingsStore from './store/settings';

// Single source of truth for authentication state
const isAuthenticated = ref(!!localStorage.getItem('auth_token'));

// Initialize settings on mount
onMounted(() => {
  settingsStore.actions.initializeSettings();
  
  // Listen for auth token changes from other tabs
  window.addEventListener('storage', (event) => {
    if (event.key === 'auth_token') {
      isAuthenticated.value = !!event.newValue;
    }
  });
});
</script>

<template>
  <el-container direction="vertical" class="app">
    <el-header class="header">
      <div class="header-content">
        <div class="logo-section">
          <h1>Meme Tagger</h1>
          <p>Upload and share your favorite memes</p>
        </div>
        <div class="nav-section">
          <UserAccount v-if="isAuthenticated" @logout="isAuthenticated = false" />
          <ApiKeySettings />
        </div>
      </div>
    </el-header>
    
    <el-main>
      <Auth v-if="!isAuthenticated" @login-success="isAuthenticated = true" />
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
  padding: 20px !important;
  height: auto !important;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.logo-section {
  display: flex;
  flex-direction: column;
}

.header h1 { 
  margin: 0;
  font-size: 2em;
}

.header p { 
  margin: 10px 0 0;
  opacity: 0.8;
}

.nav-section {
  display: flex;
  align-items: center;
  gap: 16px;
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
