<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Picture } from '@element-plus/icons-vue';
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
const route = useRoute();
const router = useRouter();

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

// Navigate to public memes page
const goToPublicMemes = () => {
  router.push('/public');
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
          <el-button type="primary" link class="public-link" @click="goToPublicMemes">
            <el-icon><Picture /></el-icon>
            Public Gallery
          </el-button>
          <UserAccount v-if="userStore.state.isAuthenticated" @logout="userStore.actions.setAuthenticated(false)" />
          <el-button v-else type="primary" @click="router.push('/')" class="login-button">
            Login / Register
          </el-button>
          <ApiKeySettings />
        </div>
      </div>
    </el-header>
    
    <!-- Main content -->
    <el-main>
      <!-- Unauthenticated -->
      <template v-if="!userStore.state.isAuthenticated && route.name !== 'public-memes'">
        <Auth @login-success="handleLoginSuccess" />
      </template>
      
      <!-- Show public memes or user content -->
      <template v-else>
        <el-tabs v-if="route.name === 'home'" v-model="activeTab" type="card" class="main-tabs">
          <el-tab-pane label="Upload" name="upload">
            <MemeUploader @refetch="memeBrowserRef?.loadMemes()" class="content"/>
          </el-tab-pane>
          <el-tab-pane label="Browse" name="browse">
            <MemeBrowser ref="memeBrowserRef" />
          </el-tab-pane>
        </el-tabs>
        <router-view v-else />
      </template>
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

/* Public gallery link */
.public-link {
  color: white !important;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 5px;
}

.public-link:hover {
  opacity: 0.8;
}

/* Login button */
.login-button {
  font-weight: bold;
}

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
