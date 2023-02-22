<template>
  <div :class="{'is-active': isActive, 'is-disabled': disabled }"
       class="el-collapse-item">
    <div
        :aria-controls="`el-collapse-content-${id}`"
        :aria-describedby="`el-collapse-content-${id}`"
        :aria-expanded="isActive"
        role="tab"
    >
      <div
          :id="`el-collapse-head-${id}`"
          :class="{
          'focusing': focusing,
          'is-active': isActive
        }"
          :tabindex="disabled ? undefined : 0"
          class="el-collapse-item__header"
          role="button"
          @blur="focusing = false"
          @click="handleHeaderClick"
          @focus="handleFocus"
          @keyup.space.enter.stop="handleEnterClick"
      >
        <slot name="title">{{ title }}</slot>
        <i
            :class="{'is-active': isActive}"
            class="el-collapse-item__arrow el-icon-arrow-right">
        </i>
      </div>
    </div>
    <el-collapse-transition>
      <div
          v-show="isActive"
          :id="`el-collapse-content-${id}`"
          :aria-hidden="!isActive"
          :aria-labelledby="`el-collapse-head-${id}`"
          class="el-collapse-item__wrap"
          role="tabpanel"
      >
        <div class="el-collapse-item__content">
          <slot></slot>
        </div>
      </div>
    </el-collapse-transition>
  </div>
</template>
<script>
import ElCollapseTransition from 'element-ui/src/transitions/collapse-transition';
import Emitter from 'element-ui/src/mixins/emitter';
import {generateId} from 'element-ui/src/utils/util';

export default {
  name: 'ElCollapseItem',

  componentName: 'ElCollapseItem',

  mixins: [Emitter],

  components: {ElCollapseTransition},

  data() {
    return {
      contentWrapStyle: {
        height: 'auto',
        display: 'block'
      },
      contentHeight: 0,
      focusing: false,
      isClick: false,
      id: generateId()
    };
  },

  inject: ['collapse'],

  props: {
    title: String,
    name: {
      type: [String, Number],
      default() {
        return this._uid;
      }
    },
    disabled: Boolean
  },

  computed: {
    isActive() {
      return this.collapse.activeNames.indexOf(this.name) > -1;
    }
  },

  methods: {
    handleFocus() {
      setTimeout(() => {
        if (!this.isClick) {
          this.focusing = true;
        } else {
          this.isClick = false;
        }
      }, 50);
    },
    handleHeaderClick() {
      if (this.disabled) return;
      this.dispatch('ElCollapse', 'item-click', this);
      this.focusing = false;
      this.isClick = true;
    },
    handleEnterClick() {
      this.dispatch('ElCollapse', 'item-click', this);
    }
  }
};
</script>
