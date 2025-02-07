<template>
  <div id="app">
    <Calculator/>
    <ContactForm/>
  </div>
</template>

<script>
import Calculator from './components/Calculator.vue';
import ContactForm from './components/ContactForm.vue';

export default {
  name: 'App',
  components: {
    Calculator,
    ContactForm,
  },
};
</script>
