<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService, openaiService } from '../services/api';

// File upload state
const selectedFile = ref<File | null>(null);
const previewUrl = ref('');
const uploading = ref(false);
const uploadedImageUrl = ref('');

// Analysis state
const analyzing = ref(false);
const imageAnalysis = ref('');

// Processing state (combined uploading and analyzing)
const processing = ref(false);

// Handle file selection
const handleFileChange = (file: File | null) => {
  if (!file) return false;
  
  if (!file.type.startsWith('image/')) {
    ElMessage.error('Please upload an image file');
    return false;
  }
  
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('File too large (max 5MB)');
    return false;
  }

  selectedFile.value = file;
  previewUrl.value = URL.createObjectURL(file);
  return false;
};

// Upload the selected file and analyze it
const uploadAndAnalyze = async () => {
  if (!selectedFile.value) return;
  
  processing.value = true;
  uploading.value = true;
  
  try {
    // Upload step
    const res = await uploadService.uploadImage(selectedFile.value);
    if (res.data?.code === 1) {
      uploadedImageUrl.value = res.data.data;
      uploading.value = false;
      
      // Analyze step
      analyzing.value = true;
      const result = await openaiService.analyzeImage(uploadedImageUrl.value);
      if (result.success) {
        imageAnalysis.value = result.data || '';
        ElMessage.success('Meme tagged successfully');
      } else {
        ElMessage.error(`Analysis failed: ${result.error}`);
      }
    } else {
      ElMessage.error('Upload failed');
    }
  } catch (error) {
    ElMessage.error('Process failed');
    console.error(error);
  } finally {
    uploading.value = false;
    analyzing.value = false;
    processing.value = false;
  }
};

// Copy the image URL to clipboard
const copyUrl = async () => {
  try {
    await navigator.clipboard.writeText(uploadedImageUrl.value);
    ElMessage.success('Copied to clipboard');
  } catch {
    ElMessage.error('Failed to copy to clipboard');
  }
};
</script>

<template>
  <el-card>
    <template #header>Upload a Meme</template>
    
    <!-- File upload area -->
    <el-upload
      drag
      action="#"
      :auto-upload="false"
      :on-change="(file: any) => handleFileChange(file.raw)"
      :show-file-list="false"
    >
      <div class="center">
        <el-icon><Upload /></el-icon>
        <div>Drop image here or <em>click to upload</em></div>
        <div class="tip">JPG/PNG images, max 5MB</div>
      </div>
    </el-upload>
    
    <!-- Image preview -->
    <div v-if="previewUrl" class="preview">
      <el-image :src="previewUrl" fit="contain" style="max-height: 300px" />
    </div>
    
    <!-- Tag and Share button -->
    <el-button 
      v-if="selectedFile" 
      type="primary" 
      @click="uploadAndAnalyze" 
      :loading="processing"
      class="btn"
    >
      {{ processing ? (uploading ? 'Uploading...' : 'Tagging...') : 'Tag and Share' }}
    </el-button>
    
    <!-- Results section -->
    <el-result v-if="uploadedImageUrl" icon="success" title="Ready to Share">
      <template #extra>
        <!-- Image URL -->
        <el-input v-model="uploadedImageUrl" readonly>
          <template #append>
            <el-button @click="copyUrl" type="primary">Copy</el-button>
          </template>
        </el-input>
        <a :href="uploadedImageUrl" target="_blank" class="link">Open Image</a>
        
        <!-- Analysis results -->
        <el-card v-if="imageAnalysis" class="analysis-card">
          <template #header>AI Tags</template>
          <p>{{ imageAnalysis }}</p>
        </el-card>
      </template>
    </el-result>
  </el-card>
</template>

<style scoped>
.center { text-align: center; }
.preview { margin: 20px 0; text-align: center; }
.tip { color: #909399; font-size: 12px; margin-top: 10px; }
.btn { display: block; margin: 20px auto; }
.link { display: block; margin-top: 10px; text-align: center; }
.analysis-card { margin-top: 20px; }
</style> 