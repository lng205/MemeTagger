import axios from 'axios';

const API_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  },
  // Important for CORS with credentials
  withCredentials: true
});

// Add a request interceptor to add the authorization token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const userService = {
  register: (username: string, password: string) => 
    api.put('/user/register', { username, password }),

  login: (username: string, password: string) => {
    // Create Basic auth token
    const token = btoa(`${username}:${password}`);
    
    // Don't include username/password in the body for Basic auth
    return api.post('/user/login', {}, {
      headers: { 
        Authorization: `Basic ${token}`,
        // Override the default Content-Type since we're not sending JSON body
        'Content-Type': 'application/json'
      }
    });
  }
};

export const uploadService = {
  uploadImage: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    
    return api.post('/common/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
};

export default api; 