<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService, openaiService } from '../services/api';

// File upload state
const selectedFile = ref(null);
const previewUrl = ref('');
const uploading = ref(false);
const uploadedImageUrl = ref('');

// Analysis state
const analyzing = ref(false);
const imageAnalysis = ref('');
const autoAnalyze = ref(true);

// Handle file selection
const handleFileChange = (file) => {
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

// Upload the selected file
const upload = async () => {
  if (!selectedFile.value) return;
  
  uploading.value = true;
  try {
    const res = await uploadService.uploadImage(selectedFile.value);
    if (res.data?.code === 1) {
      uploadedImageUrl.value = res.data.data;
      ElMessage.success('Upload successful');
      
      if (autoAnalyze.value) {
        analyzeImage();
      }
    } else {
      ElMessage.error('Upload failed');
    }
  } catch {
    ElMessage.error('Upload failed');
  } finally {
    uploading.value = false;
  }
};

// Analyze the uploaded image with AI
const analyzeImage = async () => {
  if (!uploadedImageUrl.value) return;
  
  analyzing.value = true;
  try {
    const result = await openaiService.analyzeImage(uploadedImageUrl.value);
    if (result.success) {
      imageAnalysis.value = result.data || '';
    } else {
      ElMessage.error(`Analysis failed: ${result.error}`);
    }
  } catch {
    ElMessage.error('Failed to analyze image');
  } finally {
    analyzing.value = false;
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
    <template #header>
      <div class="header-container">
        <span>Upload a Meme</span>
        <el-switch v-model="autoAnalyze" active-text="Auto-analyze" />
      </div>
    </template>
    
    <!-- File upload area -->
    <el-upload
      drag
      action="#"
      :auto-upload="false"
      :on-change="(file) => handleFileChange(file.raw)"
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
    
    <!-- Upload button -->
    <el-button 
      v-if="selectedFile" 
      type="primary" 
      @click="upload" 
      :loading="uploading"
      class="btn"
    >
      {{ uploading ? 'Uploading...' : 'Upload' }}
    </el-button>
    
    <!-- Results section -->
    <el-result v-if="uploadedImageUrl" icon="success" title="Upload Successful">
      <template #extra>
        <!-- Image URL -->
        <el-input v-model="uploadedImageUrl" readonly>
          <template #append>
            <el-button @click="copyUrl" type="primary">Copy</el-button>
          </template>
        </el-input>
        <a :href="uploadedImageUrl" target="_blank" class="link">Open Image</a>
        
        <!-- Manual analyze button -->
        <el-button 
          v-if="!autoAnalyze && !imageAnalysis"
          type="success" 
          @click="analyzeImage" 
          :loading="analyzing"
          class="analyze-btn"
        >
          {{ analyzing ? 'Analyzing...' : 'Analyze with AI' }}
        </el-button>
        
        <!-- Analysis results -->
        <el-card v-if="imageAnalysis" class="analysis-card">
          <template #header>AI Analysis</template>
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
.analyze-btn { display: block; margin: 20px auto; }
.analysis-card { margin-top: 20px; }
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 