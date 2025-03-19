<template>
  <Navigation v-if="showNavigation"/>
  <router-view></router-view>
</template>

<script>
import Navigation from './components/NavigationHeader.vue'
import { authService } from './services/AuthService';

export default {
  components: {
    Navigation
  },
  computed: {
    showNavigation() {
      return this.$route.path !== '/';
    }
  },
  data() {
    return {
      tokenCheckInterval: null
    };
  },
  mounted() {
    // Check token every 60 seconds
    this.tokenCheckInterval = setInterval(() => {
      if (authService.isLoggedIn()) {
        authService.refreshTokenIfNeeded();
      }
    }, 60000);
  },
  beforeUnmount() {
    // Clean up interval
    clearInterval(this.tokenCheckInterval);
  }
};
</script>
