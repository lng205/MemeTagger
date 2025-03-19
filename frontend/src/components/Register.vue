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
      ElMessage.success('Registration successful! You can now login.');
      emit('register-success');
    } else {
      ElMessage.error(response.data.msg || 'Registration failed');
    }
  } catch (err) {
    console.error('Registration error:', err);
    ElMessage.error('Registration failed. Please try again.');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="register-form">
    <h2>Register</h2>
    
    <el-form label-position="top">
      <el-form-item label="Username">
        <el-input 
          v-model="username"
          placeholder="Choose a username"
          prefix-icon="el-icon-user"
        />
      </el-form-item>
      
      <el-form-item label="Password">
        <el-input 
          v-model="password"
          type="password" 
          placeholder="Choose a password"
          prefix-icon="el-icon-lock"
          show-password
        />
      </el-form-item>
      
      <el-form-item label="Confirm Password">
        <el-input 
          v-model="confirmPassword"
          type="password" 
          placeholder="Confirm your password"
          prefix-icon="el-icon-lock"
          show-password
        />
      </el-form-item>
      
      <el-form-item>
        <el-button 
          type="primary" 
          @click="register"
          :loading="loading"
          style="width: 100%"
        >
          Register
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.register-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}
</style> 