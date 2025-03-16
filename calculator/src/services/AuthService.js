import axios from 'axios';

const API_URL = 'http://localhost:8080/api/user';

export const authService = {
  async login(username, password) {
    try {
      const response = await axios.post(`${API_URL}/login`, { username, password });

      if (response.data && response.status === 200) {
        localStorage.setItem('username', username);
        localStorage.setItem('user', JSON.stringify({
          username: username,
          id: response.data.id
        }));
      }

      return { success: true, data: response.data };
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Login failed. Please try again.';
      return { success: false, error: errorMessage };
    }
  },

  async logout() {
    try {
      // Get the current user from localStorage
      const username = localStorage.getItem('username');
      if (!username) {
        return { success: true }; // No user to log out
      }

      await axios.post(`${API_URL}/logout`, {}, {
        headers: {
          username: username
        }
      });

      // Clear storage regardless of backend response
      this.clearUserData();

      return { success: true };
    } catch (error) {
      console.error('Logout error:', error);

      // Even if logout fails, clear local storage
      this.clearUserData();

      return {
        success: false,
        error: error.response?.data?.message || 'Logout failed'
      };
    }
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  clearUserData() {
    localStorage.removeItem('user');
    localStorage.removeItem('username');
  },

  isLoggedIn() {
    return !!this.getCurrentUser();
  }
};
