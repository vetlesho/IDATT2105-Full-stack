import axios from 'axios';

const API_URL = 'http://localhost:8080/api/calculator';

export const calculatorService = {
  async calculate(expression) {
    try {
      const username = localStorage.getItem('username')
      const response = await axios.post(`${API_URL}/calculate`,
        { expression },
        { headers: { username } }
      )

      return response.data
    } catch (error) {
      return this.handleError(error, 'Calculation failed');
    }

  },

  async getHistory(page = 0, size) {
    const user = this.getCurrentUser();
    if (!user || !user.username) {
      throw new Error('No user logged in');
    }

    try {
      const response = await axios.get(`${API_URL}/history`, {
        headers: {
          username: user.username
        },
        params: {
          page,
          size,
          sort: 'timeStamp,desc'
        }
      });
      return response.data;
    } catch (error) {
      return this.handleError(error, 'Failed to load history');
    }
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    if (!userStr) return null;

    try {
      return JSON.parse(userStr);
    } catch (e) {
      console.error('Failed to parse user data:', e);
      localStorage.removeItem('user');
      return null;
    }
  },

  handleError(error, defaultMessage) {
    const errorMessage =
      error.response?.data?.message ||
      error.response?.data?.error ||
      defaultMessage;

    return { success: false, error: errorMessage };
  }
};
