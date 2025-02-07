import { createRouter, createWebHistory } from 'vue-router';
import Calculator from '@/components/Calculator.vue';
import ContactForm from '@/components/ContactForm.vue';
import HomePage from '@/components/HomePage.vue';

const routes = [
  { path: '/', component: HomePage},
  { path: '/Calculator', component: Calculator },
  { path: '/Contactform', component: ContactForm }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
