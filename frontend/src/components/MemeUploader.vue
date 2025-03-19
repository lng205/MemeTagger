<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService } from '../services/api';

const selectedFile = ref<File | null>(null);
const previewUrl = ref<string | null>(null);
const uploading = ref(false);
const uploadedImageUrl = ref('');

const handleFileChange = (file: File) => {
  if (!file) return false;
  
  // Validate file type and size
  if (!file.type.startsWith('image/')) {
    ElMessage.error('Please upload an image file');
    return false;
  }
  
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
    ElMessage.error('Upload failed. Please try again.');
  } finally {
    uploading.value = false;
  }
};

const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text);
  ElMessage.success('URL copied to clipboard');
};
</script>

<template>
  <el-card>
    <template #header>
      <span>Upload a Meme</span>
    </template>
    
    <el-upload
      drag
      action="#"
      :auto-upload="false"
      :on-change="(file) => handleFileChange(file.raw)"
      :show-file-list="false"
    >
      <div class="upload-content">
        <el-icon><Upload /></el-icon>
        <div class="el-upload__text">
          Drop image here or <em>click to upload</em>
        </div>
      </div>
      <template #tip>
        <div class="el-upload__tip">JPG/PNG images, max 5MB</div>
      </template>
    </el-upload>
    
    <div v-if="previewUrl" class="preview">
      <h3>Preview</h3>
      <el-image :src="previewUrl" fit="contain" style="max-height: 300px;" />
    </div>
    
    <div class="actions">
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
      title="Uploaded Successfully"
      sub-title="Your image is now available at:"
    >
      <template #extra>
        <el-input
          v-model="uploadedImageUrl"
          readonly
        >
          <template #append>
            <el-button @click="copyToClipboard(uploadedImageUrl)">
              Copy
            </el-button>
          </template>
        </el-input>
      </template>
    </el-result>
  </el-card>
</template>

<style scoped>
.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #909399;
}

.preview {
  margin: 20px 0;
  text-align: center;
}

.actions {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style> 