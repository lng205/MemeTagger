<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import settingsStore from '../store/settings';

const { state, actions } = settingsStore;
const apiKey = ref('');
const isDialogVisible = ref(false);

// Check if API key is set
const hasApiKey = computed(() => !!state.openaiApiKey);

onMounted(() => {
  // Initialize settings from localStorage
  actions.initializeSettings();
  // Set the input value from store
  apiKey.value = state.openaiApiKey;
});

const saveApiKey = () => {
  if (!apiKey.value) {
    actions.clearApiKey();
    ElMessage.success('API key cleared');
  } else {
    actions.saveApiKey(apiKey.value);
    ElMessage.success('API key saved');
  }
  isDialogVisible.value = false;
};

const showDialog = () => {
  apiKey.value = state.openaiApiKey;
  isDialogVisible.value = true;
};

const clearApiKey = () => {
  apiKey.value = '';
  actions.clearApiKey();
  ElMessage.success('API key cleared');
  isDialogVisible.value = false;
};
</script>

<template>
  <div class="api-key-settings">
    <el-button @click="showDialog" size="small">
      <el-icon><Setting /></el-icon>
      <span>{{ hasApiKey ? 'Change API Key' : 'Set API Key' }}</span>
    </el-button>
    
    <el-dialog
      v-model="isDialogVisible"
      title="OpenAI API Key Settings"
      width="500px"
      destroy-on-close
    >
      <p class="description">
        Set your OpenAI API key to use for image analysis. This key will be saved locally
        in your browser and used for all image analysis requests.
      </p>
      
      <el-form>
        <el-form-item>
          <el-input
            v-model="apiKey"
            placeholder="Enter your OpenAI API key"
            show-password
            clearable
          >
            <template #prepend>API Key</template>
          </el-input>
        </el-form-item>
        
        <div class="api-key-info">
          <el-alert
            title="Your API key is stored only in your browser's local storage."
            type="info"
            show-icon
            :closable="false"
          />
        </div>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="isDialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="clearApiKey" v-if="hasApiKey">
            Clear Key
          </el-button>
          <el-button type="primary" @click="saveApiKey">
            Save
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.api-key-settings {
  display: inline-block;
}
.description {
  margin-bottom: 20px;
  color: #606266;
}
.api-key-info {
  margin-top: 20px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 