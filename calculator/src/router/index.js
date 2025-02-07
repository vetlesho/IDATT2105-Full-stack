import { createRouter, createWebHistory } from 'vue-router';
import Calculator from '@/components/Calculator.vue';
import ContactForm from '@/components/ContactForm.vue';

const routes = [
  { path: '/', component: Calculator },
  { path: '/kontaktskjema', component: ContactForm }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
