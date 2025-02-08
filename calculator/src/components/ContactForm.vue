<template>
  <div class="contactForm">
    <h1>Give feedback!</h1>
    <p>
      <input type="text" placeholder="Full Name" v-model="name">
    </p>
    <p>
      <input type="email" placeholder="E-mail" v-model="email">
    </p>
    <p>
      <textarea placeholder="Feedback" v-model="feedback"></textarea>
    </p>
    <button @click="submitForm">Submit</button>
    <AlertPopup ref="alertPopup" :message="alertMessage" />
  </div>
</template>

<script>
import useValidate from '@vuelidate/core';
import { required } from '@vuelidate/validators';
import AlertPopup from './AlertPopup.vue';

export default {
  components: {
      AlertPopup,
  },
  data() {
    return {
      v$: useValidate(),
      name: '',
      email: '',
      feedback: '',
      alertMessage: '',
    }
  },
  validations() {
    return {
      name: { required },
      email:  { required },
      feedback:  { required },
    }
  },
  methods: {
    submitForm() {
      //console.log(this.v$);
      this.v$.$validate();
      if (!this.v$.$error) {
        this.alertMessage = 'Form successfully submitted!';
        this.$refs.alertPopup.show(); // Call show() on the alert component
      }
    },
  },
}
</script>


<style scoped>
.contactForm {
  width: 500px;
  margin: 0 auto;
  background-color: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

input {
  border: none;
  outline: none;
  border-bottom: 2px solid #ddd;
  width: 450px;
  font-size: 1em;
  padding: 5px 0;
  margin: 10px 0 5px 0;
}

textarea {
  width: 450px;
  height: 150px;
  font-size: 1em;
  outline: none;
  border: 1px solid #ddd;
  resize: none;
}

button {
  padding: 0.6rem 1.2rem;
  background-color: #242424;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
  font-weight: 500;
}

button:hover {
  background-color: #404040;
}
</style>
