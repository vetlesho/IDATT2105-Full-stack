import axios from 'axios';

const API_URL = 'http://localhost:8080/api/user';

export const authService = {
  async login(username, password) {
    try {
      const response = await axios.post(`${API_URL}/login`, {
        username,
        password
      });

      if (response.data) {
        // Store both user data and username for router guard
        localStorage.setItem('username', username);
        localStorage.setItem('user', JSON.stringify({
          username: username,
          id: response.data.id
        }));
        return response.data;
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message;
      if (errorMessage?.includes('already logged in')) {
        throw new Error('Another user is currently logged in. Please try again later.');
      }
      if (error.response?.status === 401) {
        throw new Error('Invalid username or password');
      }
      throw new Error(errorMessage || 'Login failed. Please try again.');
    }
  },

  async logout() {
    const user = this.getCurrentUser();
    if (!user) {
      console.log('No user found to logout');
      return;
    }

    try {
      // Make sure to wait for the backend logout to complete
      await axios.post(`${API_URL}/logout`, null, {
        headers: {
          username: user.username,
        }
      });
      console.log('Logout successful');
    } catch (error) {
      console.error('Logout error:', error);
      throw new Error(error.response?.data?.message || 'Failed to logout');
    } finally {
      // Only clear local data after backend confirms logout or if there's an error
      this.clearUserData();
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
