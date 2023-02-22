<template>
  <transition name="el-loading-fade" @after-leave="handleAfterLeave">
    <div
        v-show="visible"
        :class="[customClass, { 'is-fullscreen': fullscreen }]"
        :style="{ backgroundColor: background || '' }"
        class="el-loading-mask">
      <div class="el-loading-spinner">
        <svg v-if="!spinner" class="circular" viewBox="25 25 50 50">
          <circle class="path" cx="50" cy="50" fill="none" r="20"/>
        </svg>
        <i v-else :class="spinner"></i>
        <p v-if="text" class="el-loading-text">{{ text }}</p>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  data() {
    return {
      text: null,
      spinner: null,
      background: null,
      fullscreen: true,
      visible: false,
      customClass: ''
    };
  },

  methods: {
    handleAfterLeave() {
      this.$emit('after-leave');
    },
    setText(text) {
      this.text = text;
    }
  }
};
</script>
