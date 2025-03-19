<script setup lang="ts">
import { ref } from 'vue';
import { userService } from '../services/api';

const username = ref('');
const password = ref('');
const error = ref('');
const emit = defineEmits(['login-success']);

const login = async () => {
  if (!username.value || !password.value) {
    error.value = 'Please enter both username and password';
    return;
  }

  error.value = '';
  
  try {
    const response = await userService.login(username.value, password.value);
    console.log('Login response:', response);
    
    // Check if the response contains a token
    if (response.data && response.data.data) {
      // Store the token and emit success
      localStorage.setItem('auth_token', response.data.data);
      console.log('Token stored:', response.data.data);
      emit('login-success');
    } else {
      console.error('No token in response:', response.data);
      error.value = 'Login failed: No token received';
    }
  } catch (err) {
    console.error('Login error:', err);
    error.value = 'Login failed. Please try again.';
  }
};
</script>

<template>
  <div class="login-form">
    <h2>Login</h2>
    <div v-if="error" class="error">{{ error }}</div>
    
    <div class="form-group">
      <label for="username">Username</label>
      <input 
        id="username"
        v-model="username"
        type="text" 
        placeholder="Enter your username" 
      />
    </div>
    
    <div class="form-group">
      <label for="password">Password</label>
      <input 
        id="password"
        v-model="password"
        type="password" 
        placeholder="Enter your password" 
      />
    </div>
    
    <button @click="login">Login</button>
  </div>
</template>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.error {
  color: red;
  margin-bottom: 15px;
}
</style> 