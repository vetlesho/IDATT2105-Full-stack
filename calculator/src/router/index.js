import { createRouter, createWebHistory } from 'vue-router';
import Calculator from '../components/Calculator.vue';
import ContactForm from '../components/ContactForm.vue';
import LoginCalc from '../components/LoginCalc.vue';
import { authService } from '@/services/AuthService';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginCalc
    },
    {
      path: '/calculator',
      name: 'calculator',
      component: Calculator,
      meta: { requiresAuth: true }
    },
    {
      path: '/contact',
      name: 'contact',
      component: ContactForm,
      meta: { requiresAuth: true }
    }
  ]
});

// Navigation guard for JWT authentication
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  if (requiresAuth) {
    // Check if user is logged in and token is valid
    if (authService.isLoggedIn()) {
      next(); // User has valid token
    } else {
      // Token expired or invalid, redirect to login
      next('/');
    }
  } else {
    // For login page, check if already logged in
    if (to.path === '/' && authService.isLoggedIn()) {
      next('/calculator'); // Already logged in, go to calculator
    } else {
      next(); // Proceed to login page
    }
  }
});

export default router
