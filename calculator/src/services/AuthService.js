import axios from 'axios';

const API_URL = 'http://localhost:8080/api/user';

export const authService = {
  async login(username, password) {
    try {
      const response = await axios.post(`${API_URL}/login`, { username, password });
      localStorage.setItem('username', username);
      localStorage.setItem('user', JSON.stringify({ username, id: response.data.id }));
      return { success: true, data: response.data };
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Login failed. Please try again.';
      return { success: false, error: errorMessage };
    }
  },

  async logout() {
    const user = this.getCurrentUser();
    if (!user) {
      return { success: true };
    }

    try {
      await axios.post(`${API_URL}/logout`, null, { headers: { username: user.username } });
      this.clearUserData();
      return { success: true };
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Failed to logout';
      return { success: false, error: errorMessage };
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
