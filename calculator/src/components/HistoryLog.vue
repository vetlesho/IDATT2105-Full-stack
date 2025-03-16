<template>
  <div class="calculation-history">
    <div class="history-header">
      <h3>Recent Calculations</h3>
      <div class="dropdown">
        <select v-model="selectedCount" @change="loadHistory" class="count-selector">
          <option value="5">Last 5</option>
          <option value="10">Last 10</option>
          <option value="15">Last 15</option>
          <option value="20">Last 20</option>
        </select>
      </div>
    </div>
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
      calculations: [],
      selectedCount: 5
    }
  },
  async mounted() {
    await this.loadHistory()
  },
  methods: {
    async loadHistory() {
      try {
        const response = await calculatorService.getHistory(0, this.selectedCount)
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

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

}

h3 {
  margin: 0 0 1px 0;
  font-size: 1rem;
}

.dropdown {
  margin-bottom: 10px;
}

.count-selector {
  background-color: #272121;
  color: white;
  border: 1px solid #555;
  padding: 5px;
  border-radius: 4px;
  width: 100px;
  font-size: 0.8rem;
}

.history-list {
  list-style: none;
  padding: 0;
  margin: 0;
  overflow-y: auto;
  max-height: 150px;
}

.history-item {
  padding: 6px 0;
  border-bottom: 1px solid #555;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.timestamp {
  color: #aaa;
  margin-left: 8px;
}
</style>
