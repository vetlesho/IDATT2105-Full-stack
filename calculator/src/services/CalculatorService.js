import axios from 'axios';

const API_URL = 'http://localhost:8080/api/calculator';

export const calculatorService = {
  async calculate(expression) {
    try {
      const response = await axios.post(`${API_URL}/calculate`, {
        expression: expression
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.error || 'Network error. Please try again.';
    }
  }
};
