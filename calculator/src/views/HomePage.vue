<template>
  <div class="home-container">
    <h1>Welcome to the Calculator App</h1>
    <div class="nav-buttons">
      <router-link to="/Calculator" class="nav-button">Calculator</router-link>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  text-align: center;
  padding: 2rem;
}

.nav-buttons {
  display: flex;
  gap: 2rem;
  justify-content: center;
  margin-top: 2rem;
}

.nav-button {
  padding: 1rem 2rem;
  background-color: #4d4d4d;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.nav-button:hover {
  background-color: #242424;
}
</style>
