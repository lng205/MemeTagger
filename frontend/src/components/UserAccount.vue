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
    <el-dropdown v-if="username" trigger="click">
      <div class="user-dropdown-link">
        <el-avatar size="small" :icon="'User'" />
        <span>{{ username }}</span>
        <el-icon><arrow-down /></el-icon>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="logout">
            <el-icon><switch-button /></el-icon>
            <span>Logout</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <el-skeleton v-else-if="loading" :rows="1" style="width: 80px" animated />
  </div>
</template>

<style scoped>
.user-account {
  display: flex;
  align-items: center;
}
.user-dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: white;
}
</style> 