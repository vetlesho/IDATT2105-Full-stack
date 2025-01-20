<template>
  <div id="app">
    <h1>Vue Calculator</h1>
    <div class="calculator">
      <input
        type="text"
        v-model="currentValue"
        readonly
        class="calculator-screen"
      />
      <div class="buttons">
        <button @click="clear">C</button>
        <button @click="append('.')">.</button>
        <button @click="append('/')">/</button>
        <button @click="append('*')">*</button>

        <button @click="append('7')">7</button>
        <button @click="append('8')">8</button>
        <button @click="append('9')">9</button>
        <button @click="append('-')">-</button>

        <button @click="append('4')">4</button>
        <button @click="append('5')">5</button>
        <button @click="append('6')">6</button>
        <button @click="append('+')">+</button>

        <button @click="append('1')">1</button>
        <button @click="append('2')">2</button>
        <button @click="append('3')">3</button>
        <button @click="calculate">=</button>

        <button @click="append('0')" class="span-two">0</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentValue: '',
    };
  },
  methods: {
    append(value) {
      this.currentValue += value;
    },
    clear() {
      this.currentValue = '';
    },
    calculate() {
      try {
        this.currentValue = eval(this.currentValue).toString();
      } catch (e) {
        this.currentValue = 'Error';
      }
    },
  },
};
</script>

<style>
#app {
  font-family: Arial, sans-serif;
  text-align: center;
  margin-top: 50px;
}

.calculator {
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  background: #f9f9f9;
}

.calculator-screen {
  width: 100%;
  height: 50px;
  margin-bottom: 20px;
  font-size: 24px;
  text-align: right;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background: #fff;
}

.buttons {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

button {
  font-size: 20px;
  padding: 15px;
  border: none;
  border-radius: 5px;
  background: #007bff;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
}

button:hover {
  background: #0056b3;
}

.span-two {
  grid-column: span 2;
}
</style>
