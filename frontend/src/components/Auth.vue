<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import Login from './Login.vue';
import Register from './Register.vue';

const isAuthenticated = ref(false);
const activeTab = ref('login');
const emit = defineEmits(['login-success', 'logout']);

const handleLogin = () => {
  isAuthenticated.value = true;
  emit('login-success');
};

const handleRegister = () => {
  activeTab.value = 'login';
};

const logout = () => {
  localStorage.removeItem('auth_token');
  isAuthenticated.value = false;
  ElMessage.success('Logged out successfully');
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
    <el-card v-if="isAuthenticated">
      <template #header>
        <span>User Account</span>
      </template>
      <div class="text-center">
        <h3>You are logged in</h3>
        <el-button type="danger" @click="logout">Logout</el-button>
      </div>
    </el-card>
    
    <el-card v-else>
      <template #header>
        <span>Account Access</span>
      </template>
      
      <el-tabs v-model="activeTab">
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
  max-width: 500px;
  margin: 0 auto;
}

.text-center {
  text-align: center;
}
</style> 