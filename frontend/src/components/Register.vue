<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { userService } from '../services/api';

const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const loading = ref(false);
const emit = defineEmits(['register-success']);

const register = async () => {
  if (!username.value || !password.value) {
    ElMessage.warning('Please enter both username and password');
    return;
  }
  
  if (password.value !== confirmPassword.value) {
    ElMessage.warning('Passwords do not match');
    return;
  }

  loading.value = true;
  
  try {
    const response = await userService.register(username.value, password.value);
    if (response.data.code === 200) {
      ElMessage.success('Registration successful!');
      emit('register-success');
    } else {
      ElMessage.error(response.data.msg || 'Registration failed');
    }
  } catch {
    ElMessage.error('Registration failed. Please try again.');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <el-form @submit.prevent="register">
    <h2 class="form-title">Register</h2>
    
    <el-form-item>
      <el-input 
        v-model="username"
        placeholder="Username"
        autofocus
      />
    </el-form-item>
    
    <el-form-item>
      <el-input 
        v-model="password"
        type="password" 
        placeholder="Password"
        show-password
      />
    </el-form-item>
    
    <el-form-item>
      <el-input 
        v-model="confirmPassword"
        type="password" 
        placeholder="Confirm Password"
        show-password
        @keyup.enter="register"
      />
    </el-form-item>
    
    <el-button 
      type="primary" 
      @click="register"
      :loading="loading"
      style="width: 100%"
    >
      Register
    </el-button>
  </el-form>
</template>

<style scoped>
.form-title {
  text-align: center;
  margin-bottom: 20px;
}
</style> 