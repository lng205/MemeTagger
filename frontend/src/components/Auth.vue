<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Login from './Login.vue';
import Register from './Register.vue';

const isAuthenticated = ref(false);
const activeTab = ref('login');
const emit = defineEmits(['login-success', 'logout']);

const handleLogin = () => {
  console.log('Login success handler called');
  isAuthenticated.value = true;
  emit('login-success');
};

const handleRegister = () => {
  activeTab.value = 'login';
};

const logout = () => {
  localStorage.removeItem('auth_token');
  isAuthenticated.value = false;
  emit('logout');
};

onMounted(() => {
  const token = localStorage.getItem('auth_token');
  isAuthenticated.value = !!token;
  if (isAuthenticated.value) {
    emit('login-success');
  }
});

defineExpose({ isAuthenticated });
</script>

<template>
  <div class="auth-container">
    <div v-if="isAuthenticated" class="auth-logged-in">
      <h2>You are logged in</h2>
      <button @click="logout" class="logout-btn">Logout</button>
    </div>
    
    <div v-else class="auth-forms">
      <div class="tabs">
        <button 
          :class="{ active: activeTab === 'login' }" 
          @click="activeTab = 'login'"
        >
          Login
        </button>
        <button 
          :class="{ active: activeTab === 'register' }" 
          @click="activeTab = 'register'"
        >
          Register
        </button>
      </div>
      
      <div class="tab-content">
        <Login v-if="activeTab === 'login'" @login-success="handleLogin" />
        <Register v-if="activeTab === 'register'" @register-success="handleRegister" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.auth-logged-in {
  text-align: center;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.logout-btn {
  background-color: #f44336;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

.tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddd;
}

.tabs button {
  flex: 1;
  padding: 10px;
  border: none;
  background-color: #f1f1f1;
  cursor: pointer;
}

.tabs button.active {
  background-color: #4CAF50;
  color: white;
}

.tab-content {
  padding-top: 20px;
}
</style> 