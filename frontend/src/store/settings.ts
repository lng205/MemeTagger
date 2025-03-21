import { reactive, readonly } from 'vue';

interface SettingsState {
  openaiApiKey: string;
}

// Create a reactive state
const state = reactive<SettingsState>({
  openaiApiKey: ''
});

// Create actions to modify the state
const actions = {
  setOpenaiApiKey(key: string) {
    state.openaiApiKey = key;
  },
  
  // Initialize from localStorage if available
  initializeSettings() {
    const savedKey = localStorage.getItem('openai_api_key');
    if (savedKey) {
      state.openaiApiKey = savedKey;
    }
  },
  
  // Save API key to localStorage
  saveApiKey(key: string) {
    state.openaiApiKey = key;
    localStorage.setItem('openai_api_key', key);
  },
  
  // Clear API key from localStorage
  clearApiKey() {
    state.openaiApiKey = '';
    localStorage.removeItem('openai_api_key');
  }
};

export default {
  state: readonly(state),
  actions
}; 