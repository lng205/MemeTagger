<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { memeService } from '../services/api';
import userStore from '../store/user';

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

const memes = ref<Meme[]>([]);
const loading = ref(true);
const currentPage = ref(1);
const pageSize = ref(6);
const total = ref(0);

// Get current user ID from store
const userId = computed(() => userStore.state.id || 0);

// Get sorted memes (newest first)
const sortedMemes = computed(() => {
  return [...memes.value].sort((a, b) => b.id - a.id);
});

// Load memes on component mount
onMounted(() => loadMemes());

// Fetch memes with pagination
async function loadMemes() {
  loading.value = true;
  
  try {
    const response = await memeService.getMemesByUser(userId.value, currentPage.value, pageSize.value);
    
    if (response.data?.code === 1) {
      // Update with new API response format
      const responseData = response.data.data;
      memes.value = responseData.records || [];
      total.value = responseData.total || 0;
    } else {
      ElMessage.error('Failed to load memes');
    }
  } catch (error) {
    ElMessage.error('Failed to load memes');
    console.error(error);
  } finally {
    loading.value = false;
  }
}

// Format date from ISO string
const formatDate = (dateString: string) => new Date(dateString).toLocaleDateString();
</script>

<template>
  <div class="meme-browser">
    <h2 class="title">Browse Memes</h2>
    
    <!-- Loading state -->
    <el-skeleton v-if="loading" :rows="5" animated />
    
    <!-- Empty state -->
    <el-empty v-else-if="memes.length === 0" description="No memes found" />
    
    <!-- Meme grid -->
    <div v-else class="meme-grid">
      <el-card 
        v-for="meme in sortedMemes" 
        :key="meme.id" 
        class="meme-card"
        shadow="hover"
      >
        <!-- Image section -->
        <div class="meme-image-container">
          <el-image :src="meme.url" fit="cover" loading="lazy" />
        </div>
        
        <!-- Info section -->
        <div class="meme-info">
          <div class="meme-meta">
            <span class="username">{{ meme.username }}</span>
            <span class="date">{{ formatDate(meme.createdAt) }}</span>
          </div>
          
          <!-- Tags section with heading -->
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
    
    <!-- Pagination -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="total"
        @current-change="loadMemes"
      />
    </div>
  </div>
</template>

<style scoped>
.meme-browser {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.title {
  margin-bottom: 30px;
  text-align: center;
  font-size: 1.8rem;
}

.meme-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 30px;
}

.meme-card {
  transition: transform 0.2s;
  border-radius: 8px;
  overflow: hidden;
}

.meme-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.meme-image-container {
  height: 250px;
  overflow: hidden;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.meme-info {
  padding: 16px;
}

.meme-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
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
  margin-top: 10px;
}

.tags-title {
  font-size: 14px;
  margin: 0 0 8px 0;
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
  margin-top: 10px;
  color: #909399;
  font-style: italic;
  font-size: 14px;
}

.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style> 