<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService } from '../services/api';

const selectedFile = ref<File | null>(null);
const previewUrl = ref<string | null>(null);
const uploading = ref(false);
const uploadedImageUrl = ref('');

const handleFileChange = (file: File) => {
  if (!file) {
    ElMessage.warning('Please select a file');
    return false;
  }
  
  // Check if file is an image
  if (!file.type.startsWith('image/')) {
    ElMessage.error('Please upload an image file');
    return false;
  }
  
  // Check file size (max 5MB)
  const maxSizeInMB = 5;
  if (file.size / 1024 / 1024 > maxSizeInMB) {
    ElMessage.error(`File size should not exceed ${maxSizeInMB}MB`);
    return false;
  }

  selectedFile.value = file;
  previewUrl.value = URL.createObjectURL(file);
  return false; // prevent auto upload
};

const uploadMeme = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('Please select a file to upload');
    return;
  }

  uploading.value = true;

  try {
    const response = await uploadService.uploadImage(selectedFile.value);
    if (response.data.code === 200) {
      ElMessage.success('Meme uploaded successfully!');
      uploadedImageUrl.value = response.data.data;
    } else {
      ElMessage.error(response.data.msg || 'Upload failed');
    }
  } catch (err) {
    console.error('Upload error:', err);
    ElMessage.error('Upload failed. Please try again.');
  } finally {
    uploading.value = false;
  }
};
</script>

<template>
  <el-card class="meme-uploader">
    <template #header>
      <div class="card-header">
        <span>Upload a Meme</span>
      </div>
    </template>
    
    <el-upload
      class="upload-container"
      drag
      action="#"
      :auto-upload="false"
      :on-change="(file) => handleFileChange(file.raw)"
      :show-file-list="false"
    >
      <el-icon class="el-icon--upload"><i-ep-upload-filled /></el-icon>
      <div class="el-upload__text">
        Drop file here or <em>click to upload</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          JPG/PNG images, max 5MB
        </div>
      </template>
    </el-upload>
    
    <div v-if="previewUrl" class="preview">
      <h3>Preview</h3>
      <el-image :src="previewUrl" fit="contain" style="max-height: 300px;" />
    </div>
    
    <div class="upload-actions">
      <el-button 
        type="primary" 
        @click="uploadMeme" 
        :disabled="!selectedFile" 
        :loading="uploading"
      >
        {{ uploading ? 'Uploading...' : 'Upload Meme' }}
      </el-button>
    </div>
    
    <el-result
      v-if="uploadedImageUrl"
      icon="success"
      title="Meme Uploaded Successfully"
      sub-title="Your image is now available at the URL below"
    >
      <template #extra>
        <el-input
          v-model="uploadedImageUrl"
          readonly
          class="url-display"
        >
          <template #append>
            <el-button @click="navigator.clipboard.writeText(uploadedImageUrl)">
              Copy
            </el-button>
          </template>
        </el-input>
      </template>
    </el-result>
  </el-card>
</template>

<style scoped>
.meme-uploader {
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-container {
  width: 100%;
  margin-bottom: 20px;
}

.preview {
  margin: 20px 0;
  text-align: center;
}

.upload-actions {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.url-display {
  margin-top: 15px;
}
</style> 