<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService } from '../services/api';

// Define the UploadFile interface to match Element Plus types
interface UploadFile {
  raw: File;
  name: string;
  size: number;
  percentage?: number;
  status?: string;
  uid: number;
}

const selectedFile = ref<File | null>(null);
const previewUrl = ref<string | null>(null);
const uploading = ref(false);
const uploadedImageUrl = ref('');

const handleFileChange = (file: File) => {
  if (!file) return false;
  
  // Validate file
  if (!file.type.startsWith('image/')) {
    ElMessage.error('Please upload an image file');
    return false;
  }
  
  if (file.size / 1024 / 1024 > 5) {
    ElMessage.error('File size should not exceed 5MB');
    return false;
  }

  selectedFile.value = file;
  previewUrl.value = URL.createObjectURL(file);
  return false;
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
      ElMessage.success('Upload successful!');
      uploadedImageUrl.value = response.data.data;
    } else {
      ElMessage.error(response.data.msg || 'Upload failed');
    }
  } catch {
    ElMessage.error('Upload failed. Please try again.');
  } finally {
    uploading.value = false;
  }
};

const copyToClipboard = () => {
  navigator.clipboard.writeText(uploadedImageUrl.value);
  ElMessage.success('URL copied');
};
</script>

<template>
  <el-card>
    <template #header>Upload a Meme</template>
    
    <el-upload
      drag
      action="#"
      :auto-upload="false"
      :on-change="(file: UploadFile) => handleFileChange(file.raw)"
      :show-file-list="false"
    >
      <div class="text-center">
        <el-icon><Upload /></el-icon>
        <div>Drop image here or <em>click to upload</em></div>
        <div class="tip">JPG/PNG images, max 5MB</div>
      </div>
    </el-upload>
    
    <div v-if="previewUrl" class="preview">
      <el-image :src="previewUrl" fit="contain" style="max-height: 300px;" />
    </div>
    
    <div class="text-center mt-20" v-if="selectedFile">
      <el-button 
        type="primary" 
        @click="uploadMeme" 
        :loading="uploading"
      >
        {{ uploading ? 'Uploading...' : 'Upload Meme' }}
      </el-button>
    </div>
    
    <el-result
      v-if="uploadedImageUrl"
      icon="success"
      title="Upload Successful"
    >
      <template #extra>
        <el-input
          v-model="uploadedImageUrl"
          readonly
        >
          <template #append>
            <el-button @click="copyToClipboard">Copy</el-button>
          </template>
        </el-input>
      </template>
    </el-result>
  </el-card>
</template>

<style scoped>
.text-center {
  text-align: center;
}

.preview {
  margin: 20px 0;
  text-align: center;
}

.mt-20 {
  margin-top: 20px;
}

.tip {
  color: #909399;
  font-size: 12px;
  margin-top: 10px;
}
</style> 