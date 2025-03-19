<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Auth from './components/Auth.vue';
import MemeUploader from './components/MemeUploader.vue';

const auth = ref();
const isAuthenticated = ref(false);

// Check auth on component mount and watch for changes
onMounted(() => {
  // Check if token exists in localStorage
  const token = localStorage.getItem('auth_token');
  isAuthenticated.value = !!token;
  
  // Listen for storage events (if user logs in/out in another tab)
  window.addEventListener('storage', (event) => {
    if (event.key === 'auth_token') {
      isAuthenticated.value = !!event.newValue;
    }
  });
});

// Function to update auth state from child components
const updateAuthState = (state: boolean) => {
  isAuthenticated.value = state;
};
</script>

<template>
  <div class="app">
    <header>
      <h1>Meme Tagger</h1>
      <p>Upload and share your favorite memes</p>
    </header>
    
    <main>
      <Auth ref="auth" @login-success="updateAuthState(true)" @logout="updateAuthState(false)" />
      
      <div v-if="isAuthenticated" class="content">
        <MemeUploader />
      </div>
    </main>
    
    <footer>
      <p>&copy; {{ new Date().getFullYear() }} Meme Tagger</p>
    </footer>
  </div>
</template>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f5f5f5;
}

.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  background-color: #333;
  color: white;
  padding: 20px;
  text-align: center;
}

h1 {
  margin: 0;
}

main {
  flex: 1;
  padding: 20px;
}

.content {
  margin-top: 20px;
}

footer {
  background-color: #333;
  color: white;
  text-align: center;
  padding: 10px;
  margin-top: auto;
}
</style>
