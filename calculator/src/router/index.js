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

router.beforeEach((to, from, next) => {
  const currentUser = authService.getCurrentUser();

  if (to.meta.requiresAuth && !currentUser) {
    next('/')
  } else {
    next()
  }
})

export default router
