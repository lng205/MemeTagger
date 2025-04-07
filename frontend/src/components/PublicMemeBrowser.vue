<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { memeService } from '../services/api';
import { useRouter } from 'vue-router';
import userStore from '../store/user';

interface Tag {
  id: number;
  name: string;
}

interface Meme {
  id: number;
  url: string;
  username: string;
  userId: number;
  createdAt: string;
  tags: Tag[];
}

const memes = ref<Meme[]>([]);
const loading = ref(true);
const currentPage = ref(1);
const pageSize = ref(12); // Show more memes per page for public browsing
const total = ref(0);

const router = useRouter();

// Check if user is authenticated
const currentUserId = computed(() => userStore.state.id || 0);

// Get sorted memes (newest first)
const sortedMemes = computed(() => {
  return [...memes.value].sort((a, b) => b.id - a.id);
});

// Load memes on component mount
onMounted(() => loadPublicMemes());

// Fetch public memes with pagination
async function loadPublicMemes() {
  loading.value = true;
  
  try {
    const response = await memeService.getPublicMemes(currentPage.value, pageSize.value);
    
    if (response.data?.code === 1) {
      // Update with API response format
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

// Add click handler for meme card
const handleMemeClick = (memeId: number) => {
  router.push({
    path: `/meme/${memeId}`,
    query: { from: 'public' }
  });
};

// Navigate to user's meme collection
const goToUserMemes = (userId: number, event: Event) => {
  event.stopPropagation(); // Prevent triggering the card click
  event.preventDefault();
  
  // Navigate to home with a userId query parameter
  router.push({
    path: '/',
    query: { userId: userId.toString() }
  });
};
</script>

<template>
  <div class="public-meme-browser">
    <div class="page-header">
      <h1>Public Meme Gallery</h1>
      <p class="subtitle">Browse memes uploaded by the community</p>
    </div>
    
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
        @click="handleMemeClick(meme.id)"
      >
        <!-- Image section -->
        <div class="meme-image-container">
          <el-image :src="meme.url" fit="cover" loading="lazy" />
        </div>
        
        <!-- Info section -->
        <div class="meme-info">
          <div class="meme-meta">
            <a 
              href="#" 
              class="username" 
              @click.stop="(event) => goToUserMemes(meme.userId, event)"
              :title="`View all memes by ${meme.username}`"
            >
              {{ meme.username }}
            </a>
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
        @current-change="loadPublicMemes"
      />
    </div>
  </div>
</template>

<style scoped>
.public-meme-browser {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 32px;
  margin-bottom: 10px;
  color: var(--el-color-primary);
}

.subtitle {
  color: #606266;
  font-size: 16px;
}

/* Meme grid layout */
.meme-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
}

.meme-card {
  transition: transform 0.2s;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}

.meme-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.meme-image-container {
  height: 220px;
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
  cursor: pointer;
  text-decoration: none;
}

.username:hover {
  text-decoration: underline;
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
  margin-right: 0;
}

.no-tags {
  color: #909399;
  font-size: 14px;
  margin-top: 10px;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>
