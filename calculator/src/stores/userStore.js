import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    name: localStorage.getItem('name') || '',
    email: localStorage.getItem('email') || ''
  }),
  actions: {
    setUserData(name, email) {
      this.name = name;
      this.email = email;
      localStorage.setItem('name', name);
      localStorage.setItem('email', email);
    }
  }
});
