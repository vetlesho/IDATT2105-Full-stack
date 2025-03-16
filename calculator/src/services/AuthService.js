import axios from 'axios';

const API_URL = 'http://localhost:8080/api/user';

export const authService = {
  async login(username, password) {
    try {
      const response = await axios.post(`${API_URL}/login`, { username, password });

      if (response?.data && response.status === 200) {
        this.saveUserData(username, response.data.id);
      }

      return { success: true, data: response.data };
    } catch (error) {
      return this.handleAuthError(error, 'Login failed. Please try again.');
    }
  },

  async logout() {
    try {
      const username = localStorage.getItem('username');
      if (!username) {
        return { success: true }; // No user to log out
      }

      await axios.post(`${API_URL}/logout`, {}, {
        headers: {username: username }
      });

      return { success: true };
    } catch (error) {
      return this.handleAuthError(error, 'Logout failed');
    } finally {
      this.clearUserData();
    }
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  saveUserData(username, id) {
    localStorage.setItem('username', username);
    localStorage.setItem('user', JSON.stringify({
      username,
      id
    }));
  },

  clearUserData() {
    localStorage.removeItem('user');
    localStorage.removeItem('username');
  },

  handleAuthError(error, defaultMessage) {
    const errorMessage =
      error.response?.data?.message ||
      error.response?.data?.error ||
      defaultMessage;

    return { success: false, error: errorMessage };
  }
};
