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
    console.log('Login response:', response);
    
    // Check if the response contains a token
    if (response.data && response.data.data) {
      // Store the token and emit success
      localStorage.setItem('auth_token', response.data.data);
      console.log('Token stored:', response.data.data);
      ElMessage.success('Login successful');
      emit('login-success');
    } else {
      console.error('No token in response:', response.data);
      ElMessage.error('Login failed: No token received');
    }
  } catch (err) {
    console.error('Login error:', err);
    ElMessage.error('Login failed. Please try again.');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="login-form">
    <h2>Login</h2>
    
    <el-form label-position="top">
      <el-form-item label="Username">
        <el-input 
          v-model="username"
          placeholder="Enter your username"
          prefix-icon="el-icon-user"
        />
      </el-form-item>
      
      <el-form-item label="Password">
        <el-input 
          v-model="password"
          type="password" 
          placeholder="Enter your password"
          prefix-icon="el-icon-lock"
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
  </div>
</template>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}
</style> 