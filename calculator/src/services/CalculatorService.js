import axios from 'axios';

const API_URL = 'http://localhost:8080/api/calculator';

export const calculatorService = {
  async calculate(expression) {
    const username = localStorage.getItem('username')
    const response = await axios.post(`${API_URL}/calculate`,
      { expression },
      { headers: { username } }
    )
    return response.data
  },

  async getHistory(page = 0, size = 5) {
    const user = JSON.parse(localStorage.getItem('user'));
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
          size
        }
      });
      return response.data;
    } catch (error) {
      console.error('History fetch error:', error);
      throw error.response?.data?.error || 'Failed to load history';
    }
  }
};
