<script setup lang="ts">
import { ref } from 'vue';
import { userService } from '../services/api';

const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const error = ref('');
const success = ref('');
const emit = defineEmits(['register-success']);

const register = async () => {
  error.value = '';
  success.value = '';
  
  if (!username.value || !password.value) {
    error.value = 'Please enter both username and password';
    return;
  }
  
  if (password.value !== confirmPassword.value) {
    error.value = 'Passwords do not match';
    return;
  }

  try {
    const response = await userService.register(username.value, password.value);
    if (response.data.code === 200) {
      success.value = 'Registration successful! You can now login.';
      emit('register-success');
    } else {
      error.value = response.data.msg || 'Registration failed';
    }
  } catch (err) {
    error.value = 'Registration failed. Please try again.';
    console.error(err);
  }
};
</script>

<template>
  <div class="register-form">
    <h2>Register</h2>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">{{ success }}</div>
    
    <div class="form-group">
      <label for="reg-username">Username</label>
      <input 
        id="reg-username"
        v-model="username"
        type="text" 
        placeholder="Choose a username" 
      />
    </div>
    
    <div class="form-group">
      <label for="reg-password">Password</label>
      <input 
        id="reg-password"
        v-model="password"
        type="password" 
        placeholder="Choose a password" 
      />
    </div>
    
    <div class="form-group">
      <label for="confirm-password">Confirm Password</label>
      <input 
        id="confirm-password"
        v-model="confirmPassword"
        type="password" 
        placeholder="Confirm your password" 
      />
    </div>
    
    <button @click="register">Register</button>
  </div>
</template>

<style scoped>
.register-form {
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

.success {
  color: green;
  margin-bottom: 15px;
}
</style> 