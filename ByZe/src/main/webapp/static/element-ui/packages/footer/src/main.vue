<template>
  <footer :style="{ height }" class="el-footer">
    <slot></slot>
  </footer>
</template>

<script>
export default {
  name: 'ElFooter',

  componentName: 'ElFooter',

  props: {
    height: {
      type: String,
      default: '60px'
    }
  }
};
</script>
