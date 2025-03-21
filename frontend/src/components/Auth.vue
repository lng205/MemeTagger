<script setup lang="ts">
import { ref } from 'vue';
import Login from './Login.vue';
import Register from './Register.vue';

const activeTab = ref('login');
const emit = defineEmits(['login-success']);

const handleLogin = () => {
  emit('login-success');
};
</script>

<template>
  <div class="auth-container">
    <el-card>
      <template #header>Account Access</template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Login" name="login">
          <Login @login-success="handleLogin" />
        </el-tab-pane>
        <el-tab-pane label="Register" name="register">
          <Register @register-success="activeTab = 'login'" />
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

/* Fix autofill background and text color */
:deep(input:-webkit-autofill),
:deep(input:-webkit-autofill:hover),
:deep(input:-webkit-autofill:focus) {
  -webkit-box-shadow: 0 0 0 1000px var(--el-fill-color-blank, #fff) inset !important;
  -webkit-text-fill-color: var(--el-text-color-primary, #303133) !important;
  caret-color: var(--el-color-primary, #409eff);
  transition: background-color 5000s ease-in-out 0s;
}
</style> 