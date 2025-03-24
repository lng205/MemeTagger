<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Auth from './components/Auth.vue';
import MemeUploader from './components/MemeUploader.vue';
import MemeBrowser from './components/MemeBrowser.vue';
import ApiKeySettings from './components/ApiKeySettings.vue';
import UserAccount from './components/UserAccount.vue';
import settingsStore from './store/settings';
import userStore from './store/user';

// Active tab
const activeTab = ref('upload');
const memeBrowserRef = ref();

// Initialize app
onMounted(async () => {
  // Initialize settings
  settingsStore.actions.initializeSettings();
  
  // Fetch user information if token exists
  await userStore.actions.fetchCurrentUser();
  
  // Listen for auth token changes
  window.addEventListener('storage', async (event) => {
    if (event.key === 'auth_token') {
      if (event.newValue) {
        await userStore.actions.fetchCurrentUser();
      } else {
        userStore.actions.setAuthenticated(false);
      }
    }
  });
});

// Handle login success
const handleLoginSuccess = async () => {
  await userStore.actions.fetchCurrentUser();
};
</script>

<template>
  <el-container direction="vertical" class="app">
    <!-- Header -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo-section">
          <h1>Meme Tagger</h1>
          <p>Upload and share your favorite memes</p>
        </div>
        <div class="nav-section">
          <UserAccount v-if="userStore.state.isAuthenticated" @logout="userStore.actions.setAuthenticated(false)" />
          <ApiKeySettings />
        </div>
      </div>
    </el-header>
    
    <!-- Main content -->
    <el-main>
      <Auth v-if="!userStore.state.isAuthenticated" @login-success="handleLoginSuccess" />
      
      <el-tabs v-if="userStore.state.isAuthenticated" v-model="activeTab" type="card" class="main-tabs">
        <el-tab-pane label="Upload" name="upload">
          <MemeUploader @refetch="memeBrowserRef?.loadMemes()" class="content"/>
        </el-tab-pane>
        <el-tab-pane label="Browse" name="browse">
          <MemeBrowser ref="memeBrowserRef" />
        </el-tab-pane>
      </el-tabs>
    </el-main>
    
    <!-- Footer -->
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

/* Header styles */
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

.logo-section { display: flex; flex-direction: column; }
.header h1 { margin: 0; font-size: 2em; }
.header p { margin: 10px 0 0; opacity: 0.8; }
.nav-section { display: flex; align-items: center; gap: 16px; }

/* Content styles */
.main-tabs { max-width: 1200px; margin: 0 auto; }
.content { max-width: 600px; margin: 30px auto 0; }

/* Footer styles */
.footer {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary-dark-2);
  text-align: center;
  padding: 15px !important;
  height: auto !important;
  margin-top: auto;
}
</style>
