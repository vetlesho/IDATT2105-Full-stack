import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export const authService = {
  async login(username, password) {
    try {
      const response = await axios.post(`${API_URL}/login`, { username, password });

      if (response?.data && response.status === 200) {
        const token = response.data.token;
        if (token) {
          sessionStorage.setItem('jwtToken', token);

          const userInfo = this.parseJwt(token);

          this.saveUserData(username, userInfo.sub || response.data.id);
        } else {
          console.warn('No token received from server');
        }

        //this.saveUserData(username, response.data.id);
      }
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleAuthError(error, 'Login failed. Please try again.');
    }
  },

  async logout() {
    try {
      const token = this.getToken();
      if (!token) {
        return { success: true }; // No user to log out
      }

      await axios.post(`${API_URL}/logout`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      return { success: true };
    } catch (error) {
      return this.handleAuthError(error, 'Logout failed');
    } finally {
      this.clearUserData();
    }
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  saveUserData(username, id) {
    localStorage.setItem('username', username);
    localStorage.setItem('user', JSON.stringify({
      username,
      id
    }));
  },

  clearUserData() {
    sessionStorage.removeItem('jwtToken');
    localStorage.removeItem('user');
    localStorage.removeItem('username');
  },

  getToken() {
    return sessionStorage.getItem('jwtToken');
  },

  setToken(token) {
    sessionStorage.setItem('jwtToken', token);
  },

  isTokenExpired() {
    const token = this.getToken();
    if (!token) return true;

    const tokenData = this.parseJwt(token);
    const expiration = tokenData.exp * 1000; // convert to milliseconds
    return expiration <= Date.now();
  },

  async refreshTokenIfNeeded() {
    try {
      const token = this.getToken();
      if (!token) return false;

      // Check if token is about to expire (within next minute)
      if (this.isTokenExpiringSoon()) {
        console.log("Token is about to expire, refreshing...");

        // Call the backend refresh endpoint
        const response = await axios.post(`${API_URL}/refresh-token`, {}, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        // Save the new token
        if (response.data && response.data.token) {
          this.setToken(response.data.token);
          console.log("Token refreshed successfully");
          return true;
        }
      }
      return !this.isTokenExpired();
    } catch (error) {
      console.error("Failed to refresh token:", error);
      return false;
    }
  },

  isTokenExpiringSoon(secondsThreshold = 60) {
    const token = this.getToken();
    if (!token) return true;

    // Get expiration time from token
    const tokenData = this.parseJwt(token);
    if (!tokenData || !tokenData.exp) return true;

    // Calculate time remaining
    const expiration = tokenData.exp * 1000; // convert to milliseconds
    const currentTime = Date.now();
    const timeRemaining = expiration - currentTime;
    console.log(timeRemaining / 1000, " seconds left of the token.")

    // Return true if token will expire within threshold
    return timeRemaining < (secondsThreshold * 1000);
  },

  parseJwt(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      return JSON.parse(window.atob(base64));
    } catch (e) {
      return {e};
    }
  },

  isLoggedIn() {
    return this.getToken() && !this.isTokenExpired();
  },

  handleAuthError(error, defaultMessage) {
    const errorMessage =
      error.response?.data?.message ||
      error.response?.data?.error ||
      defaultMessage;

    return { success: false, error: errorMessage };
  }
};
