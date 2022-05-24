<template>
  <label
      :aria-checked="value === label"
      :aria-disabled="isDisabled"
      :class="[
      size ? 'el-radio-button--' + size : '',
      { 'is-active': value === label },
      { 'is-disabled': isDisabled },
      { 'is-focus': focus }
    ]"
      :tabindex="tabIndex"
      class="el-radio-button"
      role="radio"
      @keydown.space.stop.prevent="value = isDisabled ? value : label"
  >
    <input
        v-model="value"
        :disabled="isDisabled"
        :name="name"
        :value="label"
        class="el-radio-button__orig-radio"
        tabindex="-1"
        type="radio"
        @blur="focus = false"
        @change="handleChange"
        @focus="focus = true"
    >
    <span
        :style="value === label ? activeStyle : null"
        class="el-radio-button__inner"
        @keydown.stop>
      <slot></slot>
      <template v-if="!$slots.default">{{ label }}</template>
    </span>
  </label>
</template>
<script>
import Emitter from 'element-ui/src/mixins/emitter';

export default {
  name: 'ElRadioButton',

  mixins: [Emitter],

  inject: {
    elForm: {
      default: ''
    },
    elFormItem: {
      default: ''
    }
  },

  props: {
    label: {},
    disabled: Boolean,
    name: String
  },
  data() {
    return {
      focus: false
    };
  },
  computed: {
    value: {
      get() {
        return this._radioGroup.value;
      },
      set(value) {
        this._radioGroup.$emit('input', value);
      }
    },
    _radioGroup() {
      let parent = this.$parent;
      while (parent) {
        if (parent.$options.componentName !== 'ElRadioGroup') {
          parent = parent.$parent;
        } else {
          return parent;
        }
      }
      return false;
    },
    activeStyle() {
      return {
        backgroundColor: this._radioGroup.fill || '',
        borderColor: this._radioGroup.fill || '',
        boxShadow: this._radioGroup.fill ? `-1px 0 0 0 ${this._radioGroup.fill}` : '',
        color: this._radioGroup.textColor || ''
      };
    },
    _elFormItemSize() {
      return (this.elFormItem || {}).elFormItemSize;
    },
    size() {
      return this._radioGroup.radioGroupSize || this._elFormItemSize || (this.$ELEMENT || {}).size;
    },
    isDisabled() {
      return this.disabled || this._radioGroup.disabled || (this.elForm || {}).disabled;
    },
    tabIndex() {
      return (this.isDisabled || (this._radioGroup && this.value !== this.label)) ? -1 : 0;
    }
  },

  methods: {
    handleChange() {
      this.$nextTick(() => {
        this.dispatch('ElRadioGroup', 'handleChange', this.value);
      });
    }
  }
};
</script>
