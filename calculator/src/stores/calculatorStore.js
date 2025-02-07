import { defineStore } from 'pinia';

export const useCalculatorStore = defineStore('calculator', {
  state: () => ({
    result: 0
  }),
  actions: {
    setResult(value) {
      this.result = value;
    }
  }
});
