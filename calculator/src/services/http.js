import axios from 'axios';
import { authService } from './AuthService';
import router from '../router';

// Create axios instance
const http = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Check token before each request
http.interceptors.request.use(
  async (config) => {
    // Try to refresh token if needed
    await authService.refreshTokenIfNeeded();

    // Get token (possibly refreshed)
    const token = authService.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // Add username header for backward compatibility
    const username = localStorage.getItem('username');
    if (username) {
      config.headers.username = username;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Handle auth errors in responses
http.interceptors.response.use(
  (response) => response,
  (error) => {
    // If unauthorized, redirect to login
    if (error.response?.status === 401) {
      console.log('Authentication failed, redirecting to login');
      authService.clearUserData();
      router.push('/');
    }
    return Promise.reject(error);
  }
);

export default http;
