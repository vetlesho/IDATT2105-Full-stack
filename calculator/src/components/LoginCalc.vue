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
        <button type="submit" class="login-button">Login</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async handleLogin() {
      try {
        await axios.post('http://localhost:8080/api/user/login', {
          username: this.username,
          password: this.password
        })
        // Store username in localStorage for later use
        localStorage.setItem('username', this.username)
        this.$router.push('/calculator') // Navigate to calculator page
      } catch (error) {
        alert('Login failed: ' + error.response?.data?.error || 'Unknown error')
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
  border-color: #646cff;
}

.login-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #646cff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.login-button:hover {
  background-color: #747bff;
}

.login-button:active {
  background-color: #535bf2;
}
</style>
