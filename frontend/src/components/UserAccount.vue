<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { userService } from '../services/api';

const username = ref('');
const loading = ref(true);
const emit = defineEmits(['logout']);

// Fetch username on component mount
onMounted(async () => {
  try {
    const response = await userService.getCurrentUser();
    if (response.data?.code === 1) {
      username.value = response.data.data;
    }
  } catch {
    // Silent fail - let the App handle authentication errors
  } finally {
    loading.value = false;
  }
});

// Handle logout
const logout = () => {
  localStorage.removeItem('auth_token');
  ElMessage.success('Logged out successfully');
  emit('logout');
};
</script>

<template>
  <div class="user-account">
    <div v-if="username" class="user-info">
      <el-avatar size="small" :icon="'User'" />
      <span class="username">{{ username }}</span>
      <el-button size="small" type="danger" @click="logout">Logout</el-button>
    </div>
    <el-skeleton v-else-if="loading" :rows="1" style="width: 80px" animated />
  </div>
</template>

<style scoped>
.user-account {
  display: flex;
  align-items: center;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
}
.username {
  margin: 0 10px 0 5px;
}
</style> 