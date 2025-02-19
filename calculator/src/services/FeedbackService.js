import axios from 'axios';

const API_URL = 'http://localhost:3000';

export const feedbackService = {
  async submitFeedback(feedbackData) {
    const response = await axios.post(`${API_URL}/feedback`, feedbackData);
    if (response.status === 201) {
      const messageResponse = await axios.get(`${API_URL}/messages`);
      return messageResponse.data;
    }
    throw new Error('Failed to submit feedback');
  }
};
