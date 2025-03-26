<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { memeService } from '../services/api';
import { useRoute, useRouter } from 'vue-router';

interface Tag {
  id: number;
  name: string;
}

interface Meme {
  id: number;
  url: string;
  username: string;
  createdAt: string;
  tags: Tag[];
}

const route = useRoute();
const router = useRouter();
const meme = ref<Meme | null>(null);
const loading = ref(true);

// Format date from ISO string
const formatDate = (dateString: string) => new Date(dateString).toLocaleDateString();

// Handle back navigation
const goBack = () => {
  router.push('/');
};

onMounted(async () => {
  const memeId = Number(route.params.id);
  if (!memeId) {
    ElMessage.error('Invalid meme ID');
    return;
  }

  loading.value = true;
  try {
    const response = await memeService.getMemeById(memeId);
    if (response.data?.code === 1) {
      meme.value = response.data.data;
    } else {
      ElMessage.error('Failed to load meme');
    }
  } catch (error) {
    ElMessage.error('Failed to load meme');
    console.error(error);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="meme-detail">
    <!-- Back button -->
    <div class="back-button">
      <el-button @click="goBack" plain>
        <el-icon><ArrowLeft /></el-icon>
        Back to Browse
      </el-button>
    </div>
    
    <!-- Loading state -->
    <el-skeleton v-if="loading" :rows="5" animated />
    
    <!-- Error state -->
    <el-empty v-else-if="!meme" description="Meme not found" />
    
    <!-- Meme content -->
    <div v-else class="meme-content">
      <el-card class="meme-card" shadow="hover">
        <!-- Image section -->
        <div class="meme-image-container">
          <el-image :src="meme.url" fit="contain" />
        </div>
        
        <!-- Info section -->
        <div class="meme-info">
          <div class="meme-meta">
            <span class="username">Posted by {{ meme.username }}</span>
            <span class="date">on {{ formatDate(meme.createdAt) }}</span>
          </div>
          
          <!-- Tags section -->
          <div class="tags-section" v-if="meme.tags?.length">
            <h4 class="tags-title">Tags</h4>
            <div class="tags-container">
              <el-tag 
                v-for="tag in meme.tags" 
                :key="tag.id"
                size="default"
                effect="light"
                class="tag"
              >
                #{{ tag.name }}
              </el-tag>
            </div>
          </div>
          
          <!-- No tags message -->
          <div v-else class="no-tags">
            <span>No tags available</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.meme-detail {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.back-button {
  margin-bottom: 20px;
}

.meme-content {
  display: flex;
  justify-content: center;
}

.meme-card {
  width: 100%;
  max-width: 800px;
  border-radius: 8px;
  overflow: hidden;
}

.meme-image-container {
  width: 100%;
  overflow: hidden;
  border-bottom: 1px solid var(--el-border-color-lighter);
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  padding: 20px;
}

.meme-image-container :deep(.el-image) {
  max-width: 100%;
  height: auto;
  object-fit: contain;
}

.meme-info {
  padding: 20px;
}

.meme-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  font-size: 14px;
}

.username {
  font-weight: bold;
  color: var(--el-color-primary);
}

.date {
  color: #909399;
}

.tags-section {
  margin-top: 15px;
}

.tags-title {
  font-size: 16px;
  margin: 0 0 10px 0;
  color: #606266;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  margin: 0;
  font-size: 0.9rem;
}

.no-tags {
  margin-top: 15px;
  color: #909399;
  font-style: italic;
  font-size: 14px;
}
</style> 