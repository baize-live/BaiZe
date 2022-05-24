<template>
  <transition name="el-zoom-in-top" @after-leave="doDestroy">
    <div
        v-show="showPopper"
        class="el-color-dropdown">
      <div class="el-color-dropdown__main-wrapper">
        <hue-slider ref="hue" :color="color" style="float: right;" vertical></hue-slider>
        <sv-panel ref="sl" :color="color"></sv-panel>
      </div>
      <alpha-slider v-if="showAlpha" ref="alpha" :color="color"></alpha-slider>
      <predefine v-if="predefine" :color="color" :colors="predefine"></predefine>
      <div class="el-color-dropdown__btns">
        <span class="el-color-dropdown__value">
          <el-input
              v-model="customInput"
              :validate-event="false"
              size="mini"
              @blur="handleConfirm"
              @keyup.native.enter="handleConfirm">
          </el-input>
        </span>
        <el-button
            class="el-color-dropdown__link-btn"
            size="mini"
            type="text"
            @click="$emit('clear')">
          {{ t('el.colorpicker.clear') }}
        </el-button>
        <el-button
            class="el-color-dropdown__btn"
            plain
            size="mini"
            @click="confirmValue">
          {{ t('el.colorpicker.confirm') }}
        </el-button>
      </div>
    </div>
  </transition>
</template>

<script>
import SvPanel from './sv-panel';
import HueSlider from './hue-slider';
import AlphaSlider from './alpha-slider';
import Predefine from './predefine';
import Popper from 'element-ui/src/utils/vue-popper';
import Locale from 'element-ui/src/mixins/locale';
import ElInput from 'element-ui/packages/input';
import ElButton from 'element-ui/packages/button';

export default {
  name: 'el-color-picker-dropdown',

  mixins: [Popper, Locale],

  components: {
    SvPanel,
    HueSlider,
    AlphaSlider,
    ElInput,
    ElButton,
    Predefine
  },

  props: {
    color: {
      required: true
    },
    showAlpha: Boolean,
    predefine: Array
  },

  data() {
    return {
      customInput: ''
    };
  },

  computed: {
    currentColor() {
      const parent = this.$parent;
      return !parent.value && !parent.showPanelColor ? '' : parent.color.value;
    }
  },

  methods: {
    confirmValue() {
      this.$emit('pick');
    },

    handleConfirm() {
      this.color.fromString(this.customInput);
    }
  },

  mounted() {
    this.$parent.popperElm = this.popperElm = this.$el;
    this.referenceElm = this.$parent.$el;
  },

  watch: {
    showPopper(val) {
      if (val === true) {
        this.$nextTick(() => {
          const {sl, hue, alpha} = this.$refs;
          sl && sl.update();
          hue && hue.update();
          alpha && alpha.update();
        });
      }
    },

    currentColor: {
      immediate: true,
      handler(val) {
        this.customInput = val;
      }
    }
  }
};
</script>
