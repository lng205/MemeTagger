<script setup lang="ts">
import { ref } from 'vue';
import { uploadService } from '../services/api';

const selectedFile = ref<File | null>(null);
const previewUrl = ref<string | null>(null);
const uploading = ref(false);
const error = ref('');
const success = ref('');
const uploadedImageUrl = ref('');

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0];
    previewUrl.value = URL.createObjectURL(selectedFile.value);
    error.value = '';
  }
};

const uploadMeme = async () => {
  if (!selectedFile.value) {
    error.value = 'Please select a file to upload';
    return;
  }

  uploading.value = true;
  error.value = '';
  success.value = '';

  try {
    const response = await uploadService.uploadImage(selectedFile.value);
    if (response.data.code === 200) {
      success.value = 'Meme uploaded successfully!';
      uploadedImageUrl.value = response.data.data;
    } else {
      error.value = response.data.msg || 'Upload failed';
    }
  } catch (err) {
    error.value = 'Upload failed. Please try again.';
    console.error(err);
  } finally {
    uploading.value = false;
  }
};
</script>

<template>
  <div class="meme-uploader">
    <h2>Upload a Meme</h2>
    
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">{{ success }}</div>
    
    <div class="upload-container">
      <div class="file-input">
        <label for="meme-file">Select Meme Image</label>
        <input 
          id="meme-file"
          type="file" 
          accept="image/*"
          @change="handleFileChange"
        />
      </div>
      
      <div v-if="previewUrl" class="preview">
        <h3>Preview</h3>
        <img :src="previewUrl" alt="Meme Preview" />
      </div>
    </div>
    
    <button 
      @click="uploadMeme" 
      :disabled="uploading || !selectedFile"
    >
      {{ uploading ? 'Uploading...' : 'Upload Meme' }}
    </button>
    
    <div v-if="uploadedImageUrl" class="uploaded-image">
      <h3>Uploaded Meme</h3>
      <p>URL: {{ uploadedImageUrl }}</p>
    </div>
  </div>
</template>

<style scoped>
.meme-uploader {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.upload-container {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.file-input {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input[type="file"] {
  display: block;
  margin-top: 10px;
}

.preview {
  margin-top: 20px;
}

.preview img {
  max-width: 100%;
  max-height: 300px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error {
  color: red;
  margin-bottom: 15px;
}

.success {
  color: green;
  margin-bottom: 15px;
}

.uploaded-image {
  margin-top: 20px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}
</style> 