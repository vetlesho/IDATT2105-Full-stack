import http from './http';

const API_URL = 'http://localhost:8080/api/calculator';

export const calculatorService = {
  async calculate(expression) {
    try {
      const response = await http.post(`${API_URL}/calculate`, { expression });
      return response.data;
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.error || 'Calculation failed'
      };
    }
  },

  async getHistory(page = 0, size = 10) {
    try {
      const response = await http.get(`${API_URL}/history`, {
        params: { page, size, sort: 'timeStamp,desc' }
      });

      return response.data;
    } catch (error) {
      console.error('History fetch error:', error);
      throw new Error(error.response?.data?.error || 'Failed to load history');
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
