import { createRouter, createWebHistory } from 'vue-router';
import Calculator from '../components/Calculator.vue';
import ContactForm from '../components/ContactForm.vue';
import LoginCalc from '../components/LoginCalc.vue';

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
  const username = localStorage.getItem('username')
  if (to.meta.requiresAuth && !username) {
    next('/')
  } else {
    next()
  }
})

export default router
