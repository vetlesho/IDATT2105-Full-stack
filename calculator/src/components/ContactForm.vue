<template>
  <div class="contactForm">
    <h1>Give feedback!</h1>
    <p>
      <input type="text" placeholder="Full Name" v-model="state.name" @blur="v$.name.$touch()">
      <span v-if="v$.name.$error">
        {{ v$.name.$errors[0].$message }}
      </span>
    </p>
    <p>
      <input type="email" placeholder="E-mail" v-model="state.email" @blur="v$.email.$touch()">
      <span v-if="v$.email.$error">
        {{ v$.email.$errors[0].$message }}
      </span>
    </p>
    <p>
      <textarea placeholder="Feedback" v-model="state.feedback" @blur="v$.feedback.$touch()"></textarea>
      <span v-if="v$.feedback.$error">
        {{ v$.feedback.$errors[0].$message }}
      </span>
    </p>
    <button @click="submitForm" :disabled="v$.$invalid" :class="{ 'disabled': v$.$invalid }">Submit</button>
    <AlertPopup ref="alertPopup" :message="alertMessage" />
  </div>
</template>

<script>
import useValidate from '@vuelidate/core';
import { required, email, minLength, helpers } from '@vuelidate/validators';
import { reactive, computed } from 'vue';
import AlertPopup from './AlertPopup.vue';

export default {
  components: {
    AlertPopup,
  },
  setup() {
    const state = reactive({
      name: '',
      email: '',
      feedback: '',
    })

    const mustBeLetters = helpers.regex(/^[A-Za-z\s]+$/)

    const rules = computed(() => {
      return {
        name: {
          required,
          minLength: minLength(6),
          mustBeLetters: helpers.withMessage('Must be only letters', mustBeLetters)
        },
        email: { required, email },
        feedback: { required, minLength: minLength(10) },
      }
    })

    const v$ = useValidate(rules, state)

    return {
      state,
      v$
    }
  },
  data() {
    return {
      alertMessage: '',
    }
  },
  methods: {
    submitForm() {
      //console.log(this.v$);
      this.v$.$validate();
      if (!this.v$.$error) {
        this.alertMessage = 'Form successfully submitted!'
        this.$refs.alertPopup.show() // Call show() on the alert component
      }
      /*else {
        this.alertMessage = "Form invalid"
        this.$refs.alertPopup.show()
      }*/
    },
  },
}
</script>

<style scoped>
.contactForm {
  width: 400px;
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
  width: 400px;
  font-size: 1em;
  padding: 5px 0;
  margin: 10px 0 5px 0;
}

textarea {
  width: 400px;
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

button.disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

button:disabled {
  pointer-events: none;
}
</style>
