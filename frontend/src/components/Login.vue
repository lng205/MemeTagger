<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { userService } from '../services/api';

const username = ref('');
const password = ref('');
const loading = ref(false);
const emit = defineEmits(['login-success']);

const login = async () => {
  if (!username.value || !password.value) {
    ElMessage.warning('Please enter both username and password');
    return;
  }

  loading.value = true;
  
  try {
    const response = await userService.login(username.value, password.value);
    
    if (response.data?.data) {
      localStorage.setItem('auth_token', response.data.data);
      ElMessage.success('Login successful');
      emit('login-success');
    } else {
      ElMessage.error('Login failed: No token received');
    }
  } catch (err) {
    ElMessage.error('Login failed. Please try again.');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <el-form label-position="top">
    <h2>Login</h2>
    
    <el-form-item label="Username">
      <el-input 
        v-model="username"
        placeholder="Enter your username"
      />
    </el-form-item>
    
    <el-form-item label="Password">
      <el-input 
        v-model="password"
        type="password" 
        placeholder="Enter your password"
        show-password
      />
    </el-form-item>
    
    <el-form-item>
      <el-button 
        type="primary" 
        @click="login"
        :loading="loading"
        style="width: 100%"
      >
        Login
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>
h2 {
  text-align: center;
  margin-bottom: 20px;
}
</style> 