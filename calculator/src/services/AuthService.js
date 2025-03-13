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
        // Store only necessary user data
        const userData = {
          username: response.data.username,
          id: response.data.id
        };
        localStorage.setItem('user', JSON.stringify(userData));
      }
      return response.data;
    } catch (error) {
      if (error.response?.status === 401) {
        throw 'Invalid username or password';
      }
      throw 'Login failed. Please try again.';
    }
  },

  logout() {
    localStorage.removeItem('user');
  },

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }
};
