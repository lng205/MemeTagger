<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService, openaiService } from '../services/api';

const selectedFile = ref<File | null>(null);
const previewUrl = ref('');
const uploading = ref(false);
const uploadedImageUrl = ref('');
const copied = ref(false);
const analyzing = ref(false);
const imageAnalysis = ref<string>('');

const handleFileChange = (file: File) => {
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

const upload = async () => {
  if (!selectedFile.value) return;
  
  uploading.value = true;
  try {
    const res = await uploadService.uploadImage(selectedFile.value);
    if (res.data.code === 1) {
      uploadedImageUrl.value = res.data.data;
      ElMessage.success('Upload successful');
    } else {
      ElMessage.error('Upload failed');
    }
  } catch {
    ElMessage.error('Upload failed');
  } finally {
    uploading.value = false;
  }
};

const analyzeImage = async () => {
  if (!uploadedImageUrl.value) return;
  
  analyzing.value = true;
  try {
    const result = await openaiService.analyzeImage(uploadedImageUrl.value, "What's in this image?");
    if (result.success) {
      imageAnalysis.value = result.data || '';
    } else {
      ElMessage.error(`Analysis failed: ${result.error}`);
    }
  } catch (error) {
    ElMessage.error('Failed to analyze image');
    console.error(error);
  } finally {
    analyzing.value = false;
  }
};

const copyUrl = async () => {
  try {
    await navigator.clipboard.writeText(uploadedImageUrl.value);
    copied.value = true;
    ElMessage.success('Copied');
  } catch (error) {
    // Modern fallback using Clipboard API
    try {
      const textArea = document.createElement('textarea');
      textArea.value = uploadedImageUrl.value;
      textArea.style.position = 'fixed';
      textArea.style.left = '-999999px';
      document.body.appendChild(textArea);
      textArea.select();
      
      // Use the promise-based clipboard API
      await navigator.clipboard.writeText(uploadedImageUrl.value);
      
      document.body.removeChild(textArea);
      copied.value = true;
      ElMessage.success('Copied');
    } catch {
      ElMessage.error('Failed to copy to clipboard');
    }
  }
};
</script>

<template>
  <el-card>
    <template #header>Upload a Meme</template>
    
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
    
    <div v-if="previewUrl" class="preview">
      <el-image :src="previewUrl" fit="contain" style="max-height: 300px" />
    </div>
    
    <el-button 
      v-if="selectedFile" 
      type="primary" 
      @click="upload" 
      :loading="uploading"
      class="btn"
    >
      {{ uploading ? 'Uploading...' : 'Upload' }}
    </el-button>
    
    <el-result v-if="uploadedImageUrl" icon="success" title="Upload Successful">
      <template #extra>
        <el-input v-model="uploadedImageUrl" readonly>
          <template #append>
            <el-button @click="copyUrl" type="primary">
              {{ copied ? 'Copied' : 'Copy' }}
            </el-button>
          </template>
        </el-input>
        <a :href="uploadedImageUrl" target="_blank" class="link">Open Image</a>
        
        <el-button 
          type="success" 
          @click="analyzeImage" 
          :loading="analyzing"
          class="analyze-btn"
        >
          {{ analyzing ? 'Analyzing...' : 'Analyze with AI' }}
        </el-button>
        
        <el-card v-if="imageAnalysis" class="analysis-card">
          <template #header>
            <div class="analysis-header">AI Analysis</div>
          </template>
          <p class="analysis-text">{{ imageAnalysis }}</p>
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
.analysis-header { font-weight: bold; }
.analysis-text { white-space: pre-wrap; }
</style> 