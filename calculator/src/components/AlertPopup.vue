<template>
  <div class="alert-popup" v-if="isVisible">
      <div class="alert-content">
          <p>{{ message }}</p>
          <button @click="closeAlert">OK</button>
      </div>
  </div>
</template>

<script>
export default {
  props: ['message'],
  data() {
    return {
      isVisible: false,
    };
  },
  methods: {
    show() {
      this.isVisible = true;
    },
    closeAlert() {
      this.isVisible = false;
      this.$emit('alert-closed');
    },
  },
};
</script>

<style scoped>
.alert-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.7);
  z-index: 1000;
}

.alert-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.alert-content p {
  margin: 0 0 10px;
}

.alert-content button {
  padding: 10px 20px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.alert-content button:hover {
  background: #0056b3;
}
</style>
