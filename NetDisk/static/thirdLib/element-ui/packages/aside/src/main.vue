<template>
  <aside :style="{ width }" class="el-aside">
    <slot></slot>
  </aside>
</template>

<script>
export default {
  name: 'ElAside',

  componentName: 'ElAside',

  props: {
    width: {
      type: String,
      default: '300px'
    }
  }
};
</script>
