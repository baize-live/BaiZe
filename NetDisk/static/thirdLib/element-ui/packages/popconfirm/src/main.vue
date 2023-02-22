<template>
  <el-popover
      v-model="visible"
      trigger="click"
      v-bind="$attrs"
  >
    <div class="el-popconfirm">
      <p class="el-popconfirm__main">
        <i
            v-if="!hideIcon"
            :class="icon"
            :style="{color: iconColor}"
            class="el-popconfirm__icon"
        ></i>
        {{ title }}
      </p>
      <div class="el-popconfirm__action">
        <el-button
            :type="cancelButtonType"
            size="mini"
            @click="cancel"
        >
          {{ cancelButtonText }}
        </el-button>
        <el-button
            :type="confirmButtonType"
            size="mini"
            @click="confirm"
        >
          {{ confirmButtonText }}
        </el-button>
      </div>
    </div>
    <slot slot="reference" name="reference"></slot>
  </el-popover>
</template>

<script>
import ElPopover from 'element-ui/packages/popover';
import ElButton from 'element-ui/packages/button';
import {t} from 'element-ui/src/locale';

export default {
  name: 'ElPopconfirm',
  props: {
    title: {
      type: String
    },
    confirmButtonText: {
      type: String,
      default: t('el.popconfirm.confirmButtonText')
    },
    cancelButtonText: {
      type: String,
      default: t('el.popconfirm.cancelButtonText')
    },
    confirmButtonType: {
      type: String,
      default: 'primary'
    },
    cancelButtonType: {
      type: String,
      default: 'text'
    },
    icon: {
      type: String,
      default: 'el-icon-question'
    },
    iconColor: {
      type: String,
      default: '#f90'
    },
    hideIcon: {
      type: Boolean,
      default: false
    }
  },
  components: {
    ElPopover,
    ElButton
  },
  data() {
    return {
      visible: false
    };
  },
  methods: {
    confirm() {
      this.visible = false;
      this.$emit('onConfirm');
    },
    cancel() {
      this.visible = false;
      this.$emit('onCancel');
    }
  }
};
</script>
