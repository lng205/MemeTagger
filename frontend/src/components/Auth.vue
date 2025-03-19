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
    <el-card v-if="isAuthenticated" class="auth-logged-in">
      <template #header>
        <div class="card-header">
          <span>Authentication Status</span>
        </div>
      </template>
      <h2>You are logged in</h2>
      <el-button type="danger" @click="logout">Logout</el-button>
    </el-card>
    
    <el-card v-else class="auth-forms">
      <template #header>
        <div class="card-header">
          <span>Account Access</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" class="auth-tabs">
        <el-tab-pane label="Login" name="login">
          <Login @login-success="handleLogin" />
        </el-tab-pane>
        <el-tab-pane label="Register" name="register">
          <Register @register-success="handleRegister" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
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
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.auth-tabs {
  margin-top: 10px;
}
</style> 