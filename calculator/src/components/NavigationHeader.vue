<template>
  <nav class="nav-header">
    <div class="nav-content">
      <h1>Calculator App</h1>
      <div class="nav-links">
        <router-link to="/" class="nav-link">Home</router-link>
        <router-link
          :to="currentRoute === '/Calculator' ? '/Contactform' : '/Calculator'"
          class="nav-link"
        >
          {{ currentRoute === '/Calculator' ? 'Contact Form' : 'Calculator' }}
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  computed: {
    currentRoute() {
      return this.$route.path;
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
  background-color: #4d4d4d;
  z-index: 1000;
}

.nav-content {
  max-width: 1280px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-header h1 {
  color: white;
  margin: 0;
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  padding: 0.6rem 1.2rem;
  background-color: #242424;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
  font-weight: 500;
}

.nav-link:hover {
  background-color: #404040;
}
</style>
