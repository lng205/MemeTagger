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
    if (error.response?.status === 401) {
      localStorage.removeItem('auth_token');
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
  getTagsByMemeId: (memeId: number) => api.get(`/tag/${memeId}`),
  setTagsForMeme: (memeId: number, tags: string[]) => api.post(`/tag/${memeId}`, tags)
};

export const memeService = {
  getMemesByUser: (userId: number, page: number = 1, pageSize: number = 10) => 
    api.get('/meme', { params: { userId, page, pageSize } }),
  getMemeById: (id: number) => api.get(`/meme/${id}`)
};

// Initialize OpenAI client
const createOpenAIClient = (apiKey?: string) => {
  const key = apiKey || settingsStore.state.openaiApiKey || import.meta.env.VITE_OPENAI_API_KEY;
  return new OpenAI({
    apiKey: key,
    dangerouslyAllowBrowser: true
  });
};

export const openaiService = {
  analyzeImage: async (imageUrl: string, apiKey?: string) => {
    try {
      const openai = createOpenAIClient(apiKey);
      
      const response = await openai.responses.create({
        model: "gpt-4o-mini",
        input: [{
          role: "user",
          content: [
            { type: "input_text", text: MEME_ANALYSIS_PROMPT },
            { type: "input_image", image_url: imageUrl, detail: "auto" },
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