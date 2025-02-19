<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="calculator-container">
    <div class="calculator">
      <div class="display">
        <input v-model="input" disabled />
      </div>
      <div class="buttons">
        <button class="funButton" @click="clearInput">C</button>
        <button class="funButton" @click="delInput">DEL</button>
        <button class="funButton" @click="clearLog">C-log</button>
        <button class="funButton" @click="appendToInput('/')">/</button>

        <button class="numButton" @click="appendToInput(7)">7</button>
        <button class="numButton" @click="appendToInput(8)">8</button>
        <button class="numButton" @click="appendToInput(9)">9</button>
        <button class="funButton" @click="appendToInput('*')">*</button>

        <button class="numButton" @click="appendToInput(4)">4</button>
        <button class="numButton" @click="appendToInput(5)">5</button>
        <button class="numButton" @click="appendToInput(6)">6</button>
        <button class="funButton" @click="appendToInput('-')">-</button>

        <button class="numButton" @click="appendToInput(1)">1</button>
        <button class="numButton" @click="appendToInput(2)">2</button>
        <button class="numButton" @click="appendToInput(3)">3</button>
        <button class="funButton" @click="appendToInput('+')">+</button>

        <button class="numButton" @click="appendToInput(0)">0</button>
        <button class="numButton" @click="appendToInput('.')">.</button>
        <button class="funButton" @click="changeValue()">+/-</button>
        <button class="resButton" @click="calculate">=</button>
      </div>
      <div class="error-message" v-if="showError"> /** conditional rendering v-if */
        {{ errorMessage }}
      </div>
    </div>
    <div class="output">
      <h3>Logg:</h3>
      <div class="outputLog">
        <p v-for="(entry, index) in calculationLog" :key="index">
          {{ entry }}
        </p>
      </div>
    </div>
    <AlertPopup ref="alertPopup"/>
  </div>
</template>

<script>
import { calculatorService } from '@/services/CalculatorService';
import AlertPopup from './AlertPopup.vue';

export default {
  components: {
    AlertPopup,
  },
  data() {
    return {
      input: '',
      calculationLog: [],
      showError: false, // for test to not give warnings
    };
  },
  methods: {
    appendToInput(value) {
      const operators = ['+', '-', '*', '/', '%'];
      const lastChar = this.input.slice(-1);

      // Check if the value is an operator
      if (operators.includes(value)) {
        const isMultiplyOrDivide = ['*', '/'].includes(lastChar);
        const isOperator = operators.includes(lastChar);

        // Prevent operator after operator (except minus after * or /)
        if (isOperator) {
          if (value === '-' && isMultiplyOrDivide) {
            this.input += value;
          }
          return;
        }

        // Don't allow operators at start (except minus)
        if (this.input === '' && value !== '-') {
          return;
        }

        this.input += value;
        return;
      }

      // Handle non-operator inputs (numbers and decimal point)
      if (value === '.') {
        const parts = this.input.split(/[+-/*%]/);
        const currentNumber = parts[parts.length - 1];
        if (currentNumber.includes('.')) {
          return;
        }
      }

      this.input += value;
    },
    clearInput() {
      this.input = '';
    },
    delInput() {
      this.input = this.input.slice(0, -1);
    },
    clearLog() {
      this.calculationLog = [];
    },
    changeValue() {
      if (this.input !== '' && !isNaN(this.input)) {
        const num = parseFloat(this.input);
        this.input = (-num).toString();
      }
    },
    async calculate() {
      try {
        const result = await calculatorService.calculate(this.input);
        this.calculationLog.unshift(`${this.input} = ${result.result}`);
        this.input = result.result.toString();
      } catch (error) {
        this.$refs.alertPopup.showAlert("Error: " + error);
      }
    },
  },
};
</script>

<style scoped>
.calculator-container {
  display: flex;
  flex-direction: column;
}

.calculator {
  align-items: center;
  margin: 10px auto;
  border: 2px solid #333;
  background-color: #242424;
  padding: 10px;
  border-radius: 10px;
}

.display input {
  text-align: right;
  padding: 20px;
  margin: 15px;
  height: 50px;
  font-size: 25px;
  color: white;
  background-color: #616161;
  border: 1px solid #333;
  border-radius: 5px;
}

.buttons {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  width: 100%;
  gap: 10px;
}

button {
  aspect-ratio: 1;
  padding: 5px;
  font-size: 25px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  color: white;
  transition: background-color 0.2s ease;
}

.funButton {
  background-color: #555555;

}

.numButton {
  background-color: #8a8a8a;
}

.resButton {
  background-color: #b45511;
}

button:hover {
  opacity: 0.7;
}

.output {
  background-color: #4d4d4d;
  padding: 10px;
  min-height: 125px;
  max-height: 125px;
  max-width: 380px;
  border: 2px solid #333;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
}

.outputLog {
  overflow-y: auto;
  max-height: 125px;
  padding-right: 10px;
  scrollbar-width: thin;
  scrollbar-color: #333 #4d4d4d;
}

.output h3 {
  text-align: left;
  margin-top: 0px;
  color: white;
}

.outputLog p {
  margin: 2px;
  margin-top: 0px;
  text-align: left;
  color: white;
}
</style>
