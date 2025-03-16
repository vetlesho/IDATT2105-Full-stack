<template>
  <nav class="nav-header">
    <div class="nav-content">
      <div class="nav-links">
        <router-link to="/calculator">Calculator</router-link>
        <router-link to="/contact">Contact Form</router-link>
      </div>
      <div class="user-info">
        <span> Currently logged in as: {{ username }}</span>
        <button @click="handleLogout" class="logout-btn">Logout</button>
      </div>
    </div>
    <AlertPopup ref="alertPopup"/>
  </nav>
</template>

<script>

import { authService } from '@/services/AuthService'
import AlertPopup from './AlertPopup.vue'

export default {
  components: {
    AlertPopup
  },
  computed: {
    username() {
      const user = authService.getCurrentUser()
      return user ? user.username : ''
    }
  },
  methods: {
    async handleLogout() {
      try {
        const result = await authService.logout();

        if (result.success) {
          this.$router.push('/');
        } else {
          this.$refs.alertPopup.showAlert(result.error);
          // Force navigation to login even if logout fails
          this.$router.push('/');
        }
      } catch (error) {
        console.error('Logout error:', error);
        this.$refs.alertPopup.showAlert('Unexpected error during logout');
        this.$router.push('/');
      }
    }
  }
}
</script>

<style scoped>
.nav-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 10px;
  font-size: 1.5rem;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.nav-links a {
  margin-right: 20px;
  color: var(--color-text);
  text-decoration: none;
}

.nav-links a.router-link-active {
  color: var(--color-heading);
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 2rem;
}
.logout-btn {
  background-color: #242424;
  color: white;
  border: 1px solid transparent;
  padding: 0.6rem 1.2rem;
  font-size: 1rem;
  font-weight: 500;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logout-btn:hover {
  background-color: #6a6a6a ;
}

.logout-btn:active {
  background-color: #242424;
  transform: translateY(1px);
}
</style>
