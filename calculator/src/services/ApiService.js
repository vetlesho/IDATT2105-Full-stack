import axios from "axios";
import { authService } from "./AuthService";
import router from "../router";

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add a request interceptor to include JWT token in all requests
api.interceptors.request.use(
  config => {
    const token = authService.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Add a response interceptor to handle token expiration
api.interceptors.response.use(
  response => response,
  error => {
    // Handle 401 Unauthorized or 403 Forbidden responses
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      console.log('Token expired or invalid. Redirecting to login...');
      authService.clearUserData();

      // Redirect to login page
      if (router.currentRoute.value.path !== '/') {
        router.push('/');
      }
    }
    return Promise.reject(error);
  }
);

export default api;

