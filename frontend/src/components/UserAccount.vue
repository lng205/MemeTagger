<script setup lang="ts">
import { ElMessage } from 'element-plus';
import userStore from '../store/user';
import { useRouter } from 'vue-router';

const router = useRouter();
const emit = defineEmits(['logout']);

// Handle logout
const logout = () => {
  userStore.actions.logout();
  ElMessage.success('Logged out successfully');
  emit('logout');
};

// Navigate to personal meme page
const goToPersonalPage = () => {
  router.push('/');
};
</script>

<template>
  <div class="user-account">
    <div v-if="userStore.state.username" class="user-info">
      <el-avatar size="small" :icon="'User'" />
      <a 
        href="#" 
        class="username"
        @click.prevent="goToPersonalPage"
        title="View my memes"
      >
        {{ userStore.state.username }}
      </a>
      <el-button size="small" type="danger" @click="logout">Logout</el-button>
    </div>
    <el-skeleton v-else-if="userStore.state.loading" :rows="1" style="width: 80px" animated />
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
  color: white;
  text-decoration: none;
  cursor: pointer;
}
.username:hover {
  text-decoration: underline;
}
</style>