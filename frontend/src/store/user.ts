import { reactive } from 'vue';
import { userService } from '../services/api';

// User state
const state = reactive({
  id: null as number | null,
  username: '',
  isAuthenticated: false,
  loading: false
});

// User actions
const actions = {
  // Fetch current user from API
  async fetchCurrentUser() {
    if (!localStorage.getItem('auth_token')) {
      state.isAuthenticated = false;
      return;
    }
    
    state.loading = true;
    
    try {
      const response = await userService.getCurrentUser();
      if (response.data?.code === 1) {
        const userData = response.data.data;
        state.id = userData.id;
        state.username = userData.username;
        state.isAuthenticated = true;
      } else {
        // If we got a response but no valid user, clear auth state
        localStorage.removeItem('auth_token');
        state.isAuthenticated = false;
      }
    } catch (error) {
      console.error('Failed to fetch user:', error);
      state.isAuthenticated = false;
    } finally {
      state.loading = false;
    }
  },
  
  // Set authentication state
  setAuthenticated(value: boolean) {
    state.isAuthenticated = value;
    if (!value) {
      state.id = null;
      state.username = '';
    }
  },
  
  // Clear user data on logout
  logout() {
    localStorage.removeItem('auth_token');
    state.id = null;
    state.username = '';
    state.isAuthenticated = false;
  }
};

export default { state, actions }; 