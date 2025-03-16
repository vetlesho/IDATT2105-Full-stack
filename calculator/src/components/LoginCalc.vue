<template>
  <div class="login-container">
    <div class="login-form">
      <h2>Calculator Login</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Username</label>
          <input
            v-model="username"
            type="text"
            required
            class="input-field"
          />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input
            v-model="password"
            type="password"
            required
            class="input-field"
          />
        </div>
        <button type="submit" class="login-button" :disabled="isLoading">
          {{ isLoading ? 'Logging in...' : 'Login' }}
        </button>
      </form>
    </div>
    <AlertPopup ref="alertPopup"/>
  </div>
</template>

<script>
import { authService } from '@/services/AuthService';
import AlertPopup from './AlertPopup.vue';

export default {
  components: {
    AlertPopup
  },
  data() {
    return {
      username: '',
      password: ''
    }
  },
  created() {
    // Ensure no user is logged in when visiting login page
    authService.clearUserData();
  },
  methods: {
    async handleLogin() {
      try {
        const result = await authService.login(this.username, this.password);

        if (result.success) {
          this.$router.push('/calculator');
        } else {
          this.$refs.alertPopup.showAlert(result.error);
        }
      } catch (error) {
        console.error('Login error:', error);
        this.$refs.alertPopup.showAlert('Unexpected error during login');
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  margin: 2rem auto;
  max-width: 400px;
  padding: 0 1rem;
}

.login-form {
  background-color: #2c2c2c;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #ffffff;
  text-align: center;
  margin-bottom: 2rem;
  font-size: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #ffffff;
}

.input-field {
  width: 90%;
  padding: 0.75rem;
  background-color: #1a1a1a;
  border: 1px solid #3f3f3f;
  border-radius: 4px;
  color: white;
  font-size: 1rem;
}

.input-field:focus {
  outline: none;
  border-color: #b45511;
}

.login-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #b45511;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.login-button:hover {
  background-color: #ff7d20;
}

.login-button:active {
  background-color: #b45511;
}
</style>
