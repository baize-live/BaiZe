<template>
  <transition name="el-alert-fade">
    <div
        v-show="visible"
        :class="[typeClass, center ? 'is-center' : '', 'is-' + effect]"
        class="el-alert"
        role="alert"
    >
      <i v-if="showIcon" :class="[ iconClass, isBigIcon ]" class="el-alert__icon"></i>
      <div class="el-alert__content">
        <span v-if="title || $slots.title" :class="[ isBoldTitle ]" class="el-alert__title">
          <slot name="title">{{ title }}</slot>
        </span>
        <p v-if="$slots.default && !description" class="el-alert__description">
          <slot></slot>
        </p>
        <p v-if="description && !$slots.default" class="el-alert__description">{{ description }}</p>
        <i v-show="closable" :class="{ 'is-customed': closeText !== '', 'el-icon-close': closeText === '' }"
           class="el-alert__closebtn" @click="close()">{{ closeText }}</i>
      </div>
    </div>
  </transition>
</template>

<script type="text/babel">
const TYPE_CLASSES_MAP = {
  'success': 'el-icon-success',
  'warning': 'el-icon-warning',
  'error': 'el-icon-error'
};
export default {
  name: 'ElAlert',

  props: {
    title: {
      type: String,
      default: ''
    },
    description: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'info'
    },
    closable: {
      type: Boolean,
      default: true
    },
    closeText: {
      type: String,
      default: ''
    },
    showIcon: Boolean,
    center: Boolean,
    effect: {
      type: String,
      default: 'light',
      validator: function (value) {
        return ['light', 'dark'].indexOf(value) !== -1;
      }
    }
  },

  data() {
    return {
      visible: true
    };
  },

  methods: {
    close() {
      this.visible = false;
      this.$emit('close');
    }
  },

  computed: {
    typeClass() {
      return `el-alert--${this.type}`;
    },

    iconClass() {
      return TYPE_CLASSES_MAP[this.type] || 'el-icon-info';
    },

    isBigIcon() {
      return this.description || this.$slots.default ? 'is-big' : '';
    },

    isBoldTitle() {
      return this.description || this.$slots.default ? 'is-bold' : '';
    }
  }
};
</script>
