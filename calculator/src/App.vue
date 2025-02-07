<template>
  <Navigation v-if="showNavigation"/>
  <router-view></router-view>
</template>

<script>
import Navigation from './components/NavigationHeader.vue'

export default {
  components: {
    Navigation
  },
  computed: {
    showNavigation() {
      return this.$route.path !== '/';
    }
  }
}
</script>
