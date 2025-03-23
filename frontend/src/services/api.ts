import axios from 'axios';
import OpenAI from 'openai';
import settingsStore from '../store/settings';
import { MEME_ANALYSIS_PROMPT } from './aiPrompts';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true
});

// Add auth token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('auth_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
}, error => Promise.reject(error));

// Handle 401 Unauthorized errors
api.interceptors.response.use(
  response => response, 
  error => {
    // If we get a 401 error, the token is invalid or expired
    if (error.response && error.response.status === 401) {
      // Clear the auth token
      localStorage.removeItem('auth_token');
      
      // Force page refresh to redirect to the login page 
      // (App.vue will handle showing the Auth component)
      window.location.reload();
    }
    return Promise.reject(error);
  }
);

export const userService = {
  register: (username: string, password: string) => 
    api.put('/user/register', { username, password }),

  login: (username: string, password: string) => {
    const token = btoa(`${username}:${password}`);
    return api.post('/user/login', {}, {
      headers: { Authorization: `Basic ${token}` }
    });
  },
  
  getCurrentUser: () => api.get('/user')
};

export const uploadService = {
  uploadImage: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    
    return api.post('/common/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};

export const tagService = {
  // Get tags for a specific meme
  getTagsByMemeId: (memeId: number) => api.get(`/tag/${memeId}`),
  
  // Set tags for a specific meme
  setTagsForMeme: (memeId: number, tags: string[]) => api.post(`/tag/${memeId}`, tags)
};

// Initialize default OpenAI client with env API key
const createOpenAIClient = (apiKey?: string) => {
  // Priority: 1. Provided key, 2. Global key in settings, 3. Environment variable
  const key = apiKey || settingsStore.state.openaiApiKey || import.meta.env.VITE_OPENAI_API_KEY;
  
  return new OpenAI({
    apiKey: key,
    dangerouslyAllowBrowser: true // Allows using API key in browser
  });
};

export const openaiService = {
  analyzeImage: async (imageUrl: string, apiKey?: string) => {
    try {
      // Create client with user-provided API key or fall back to global settings
      const openai = createOpenAIClient(apiKey);
      
      const response = await openai.responses.create({
        model: "gpt-4o-mini",
        input: [{
          role: "user",
          content: [
            { type: "input_text", text: MEME_ANALYSIS_PROMPT },
            {
              type: "input_image",
              image_url: imageUrl,
              detail: "auto"
            },
          ],
        }],
      });
      
      return {
        success: true,
        data: response.output_text || ""
      };
    } catch (error) {
      console.error('OpenAI API error:', error);
      return {
        success: false,
        error: error instanceof Error ? error.message : 'Unknown error'
      };
    }
  }
};

export default api; 