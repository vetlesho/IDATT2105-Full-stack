import axios from 'axios';

const API_URL = 'http://localhost:8080/api/calculator';

export const calculatorService = {
  async calculate(expression) {
    const user = JSON.parse(localStorage.getItem('user'));
    try {
      const response = await axios.post(`${API_URL}/calculate`,
        { expression },
        {
          headers: {
            username: user.username
          }
        }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data?.error || 'Calculation failed';
    }
  },

  async getHistory(page = 0, size = 10) {
    const user = JSON.parse(localStorage.getItem('user'));
    try {
      const response = await axios.get(`${API_URL}/history`, {
        params: { page, size },
        headers: {
          username: user.username
        }
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.error || 'Failed to fetch history';
    }
  }
};
