<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { uploadService } from '../services/api';

// Define the UploadFile interface to match Element Plus types
interface UploadFile {
  raw: File;
  uid: number;
}

const selectedFile = ref(null);
const previewUrl = ref('');
const uploading = ref(false);
const uploadedImageUrl = ref('');
const copied = ref(false);

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

const copyUrl = async () => {
  try {
    await navigator.clipboard.writeText(uploadedImageUrl.value);
    copied.value = true;
    ElMessage.success('Copied');
  } catch {
    const el = document.createElement('textarea');
    el.value = uploadedImageUrl.value;
    el.style.position = 'absolute';
    el.style.left = '-9999px';
    document.body.appendChild(el);
    el.select();
    document.execCommand('copy');
    document.body.removeChild(el);
    copied.value = true;
    ElMessage.success('Copied');
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
      :on-change="(file) => handleFileChange(file.raw)"
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
</style> 