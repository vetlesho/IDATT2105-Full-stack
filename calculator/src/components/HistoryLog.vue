<template>
  <div class="calculation-history">
    <h3>Recent Calculations</h3>
    <ul class="history-list" v-if="calculations.length > 0">
      <li v-for="calc in calculations" :key="calc.id" class="history-item">
        {{ calc.expression }} = {{ calc.result }}
        <small class="timestamp">({{ formatDate(calc.timeStamp) }})</small>
      </li>
    </ul>
    <p v-else class="no-history">No calculations yet</p>
  </div>
</template>

<script>
//import axios from 'axios'
import { calculatorService } from '../services/CalculatorService'

export default {
  data() {
    return {
      calculations: []
    }
  },
  async mounted() {
    await this.loadHistory()
  },
  methods: {
    async loadHistory() {
      try {
        const response = await calculatorService.getHistory()
        // The backend returns paginated content
        this.calculations = response.content || []
        console.log('Loaded calculations:', this.calculations)
      } catch (error) {
        console.error('Failed to load history:', error)
      }
    },
    formatDate(timestamp) {
      return new Date(timestamp).toLocaleString()
    }
  }
}
</script>

<style scoped>
.calculation-history {
  color: white;
}

.history-list {
  list-style: none;
  padding: 0;
  margin: 0;
  overflow-y: auto;
  max-height: 80px;
}

.history-item {
  padding: 4px 0;
  border-bottom: 1px solid #555;
}

.timestamp {
  color: #aaa;
  margin-left: 8px;
}

h3 {
  margin: 0 0 8px 0;
  font-size: 1.1rem;
}
</style>
