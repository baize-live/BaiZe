<template>
  <li
      :aria-disabled="disabled"
      :class="{
      'is-disabled': disabled,
      'el-dropdown-menu__item--divided': divided
    }"
      :tabindex="disabled ? null : -1"
      class="el-dropdown-menu__item"
      @click="handleClick"
  >
    <i v-if="icon" :class="icon"></i>
    <slot></slot>
  </li>
</template>
<script>
import Emitter from 'element-ui/src/mixins/emitter';

export default {
  name: 'ElDropdownItem',

  mixins: [Emitter],

  props: {
    command: {},
    disabled: Boolean,
    divided: Boolean,
    icon: String
  },

  methods: {
    handleClick(e) {
      this.dispatch('ElDropdown', 'menu-item-click', [this.command, this]);
    }
  }
};
</script>
