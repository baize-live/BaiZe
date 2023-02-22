<template>
  <div :class="shadow ? 'is-' + shadow + '-shadow' : 'is-always-shadow'" class="el-card">
    <div v-if="$slots.header || header" class="el-card__header">
      <slot name="header">{{ header }}</slot>
    </div>
    <div :style="bodyStyle" class="el-card__body">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ElCard',
  props: {
    header: {},
    bodyStyle: {},
    shadow: {
      type: String
    }
  }
};
</script>
