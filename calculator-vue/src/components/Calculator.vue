<template>
    <div class="calculator">
        <div class="display">
            <input v-model="input" disabled />
        </div>
        <div class="buttons">
            <button class="funButton" @click="clearInput">C</button>
            <button class="funButton" @click="delInput">DEL</button>
            <button class="funButton" @click="divideThousand">%</button>
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
            <button class="resButton" @click="calculateResult">=</button>
        </div>
        <div class="error-message" v-if="showError">
            {{ errorMessage }}
        </div>
        <AlertPopup :message="alertMessage" :show="showAlert" @close-alert="closeAlert" />
    </div>
    <div class="output">
        <h3>Logg:</h3>
    </div>
</template>

<script>
import AlertPopup from './AlertPopup.vue';

export default {
    components: {
        AlertPopup,
    },
    data() {
        return {
            input: '',
            showAlert: false,
            alertMessage: '',
        };
    },
    methods: {
        appendToInput(value) {
            const operators = ['+', '-', '*', '/', '%', '.'];
            const lastChar = this.input.slice(-1);

            if (operators.includes(lastChar) && operators.includes(value)) {
                return;
            }
            this.input += value;
        },
        clearInput() {
            this.input = '';
        },
        delInput() {
            this.input = this.input.slice(0, -1);
        },
        changeValue() {
            if (this.input !== '' && !isNaN(this.input)) {
                const num = parseFloat(this.input);
                this.input = (-num).toString();
            }
        },
        validateExpression(input) {
            const invalidEnd = /[+\-*/%]$/;
            if (invalidEnd.test(input)) {
                throw new Error('Expression cannot end with an operator');
            }
        },
        checkDivisionByZero(input) {
            if (input.includes('/')) {
                const parts = input.split('/');
                const denominator = eval(parts[parts.length - 1]);
                if (denominator == 0) {
                    throw new Error('Cannot divide by zero');
                }
            }
        },
        calculateResult() {
            try {
                this.validateExpression(this.input);
                this.checkDivisionByZero(this.input);
                this.input = eval(this.input).toString();
            } catch (error) {
                this.showAlertPopup(error);
            }
        },
        showAlertPopup(message) {
            this.alertMessage = message;
            this.showAlert = true;
        },
        closeAlert() {
            this.showAlert = false;
        },
    },
};
</script>

<style scoped>
.calculator {
    align-items: center;
    margin: 20px auto;
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
    justify-content: space-between;
    width: 100%;
}

button {
    padding: 24px;
    margin: 5px;
    font-size: 20px;
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
    min-height: 100px;
    border: 2px solid #333;
    border-radius: 10px;
}

.output h3 {
    text-align: left;
    margin-top: 0px;
    color: white;
}
</style>