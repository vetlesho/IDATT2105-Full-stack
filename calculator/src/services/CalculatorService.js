import axios from 'axios';

const API_URL = 'http://localhost:8080/api/calculator';

export const calculatorService = {
  async calculate(expression) {
    try {
      // Get username from localStorage
      const username = localStorage.getItem('username');
      if (!username) {
        return {
          success: false,
          error: 'No user logged in. Please login first.'
        };
      }

      // Send request with JWT token AND username header
      const token = sessionStorage.getItem('jwtToken');
      const headers = {
        'username': username
      };

      // Add Authorization header if token exists
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }

      const response = await axios.post(
        `${API_URL}/calculate`,
        { expression },
        { headers }
      );

      return response.data;
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.error || 'Calculation failed'
      }

    }

  },

  async getHistory(page = 0, size = 10) {
    try {
      // Get username from localStorage
      const username = localStorage.getItem('username');
      if (!username) {
        throw new Error('No user logged in');
      }

      // Send request with JWT token AND username header
      const token = sessionStorage.getItem('jwtToken');
      const headers = {
        'username': username
      };

      // Add Authorization header if token exists
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }

      console.log('Getting history with username:', username);

      const response = await axios.get(`${API_URL}/history`, {
        headers,
        params: {
          page,
          size,
          sort: 'timeStamp,desc'
        }
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
