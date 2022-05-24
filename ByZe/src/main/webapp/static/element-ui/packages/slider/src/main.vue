<template>
  <div
      :aria-disabled="sliderDisabled"
      :aria-orientation="vertical ? 'vertical': 'horizontal'"
      :aria-valuemax="max"
      :aria-valuemin="min"
      :class="{ 'is-vertical': vertical, 'el-slider--with-input': showInput }"
      class="el-slider"
      role="slider"
  >
    <el-input-number
        v-if="showInput && !range"
        ref="input"
        v-model="firstValue"
        :controls="showInputControls"
        :debounce="debounce"
        :disabled="sliderDisabled"
        :max="max"
        :min="min"
        :size="inputSize"
        :step="step"
        class="el-slider__input"
        @change="emitChange">
    </el-input-number>
    <div
        ref="slider"
        :class="{ 'show-input': showInput, 'disabled': sliderDisabled }"
        :style="runwayStyle"
        class="el-slider__runway"
        @click="onSliderClick">
      <div
          :style="barStyle"
          class="el-slider__bar">
      </div>
      <slider-button
          ref="button1"
          v-model="firstValue"
          :tooltip-class="tooltipClass"
          :vertical="vertical">
      </slider-button>
      <slider-button
          v-if="range"
          ref="button2"
          v-model="secondValue"
          :tooltip-class="tooltipClass"
          :vertical="vertical">
      </slider-button>
      <div
          v-for="(item, key) in stops"
          v-if="showStops"
          :key="key"
          :style="getStopStyle(item)"
          class="el-slider__stop">
      </div>
      <template v-if="markList.length > 0">
        <div>
          <div
              v-for="(item, key) in markList"
              :key="key"
              :style="getStopStyle(item.position)"
              class="el-slider__stop el-slider__marks-stop">
          </div>
        </div>
        <div class="el-slider__marks">
          <slider-marker
              v-for="(item, key) in markList" :key="key"
              :mark="item.mark"
              :style="getStopStyle(item.position)">
          </slider-marker>
        </div>
      </template>
    </div>
  </div>
</template>

<script type="text/babel">
import ElInputNumber from 'element-ui/packages/input-number';
import SliderButton from './button.vue';
import SliderMarker from './marker';
import Emitter from 'element-ui/src/mixins/emitter';

export default {
  name: 'ElSlider',

  mixins: [Emitter],

  inject: {
    elForm: {
      default: ''
    }
  },

  props: {
    min: {
      type: Number,
      default: 0
    },
    max: {
      type: Number,
      default: 100
    },
    step: {
      type: Number,
      default: 1
    },
    value: {
      type: [Number, Array],
      default: 0
    },
    showInput: {
      type: Boolean,
      default: false
    },
    showInputControls: {
      type: Boolean,
      default: true
    },
    inputSize: {
      type: String,
      default: 'small'
    },
    showStops: {
      type: Boolean,
      default: false
    },
    showTooltip: {
      type: Boolean,
      default: true
    },
    formatTooltip: Function,
    disabled: {
      type: Boolean,
      default: false
    },
    range: {
      type: Boolean,
      default: false
    },
    vertical: {
      type: Boolean,
      default: false
    },
    height: {
      type: String
    },
    debounce: {
      type: Number,
      default: 300
    },
    label: {
      type: String
    },
    tooltipClass: String,
    marks: Object
  },

  components: {
    ElInputNumber,
    SliderButton,
    SliderMarker
  },

  data() {
    return {
      firstValue: null,
      secondValue: null,
      oldValue: null,
      dragging: false,
      sliderSize: 1
    };
  },

  watch: {
    value(val, oldVal) {
      if (this.dragging ||
          Array.isArray(val) &&
          Array.isArray(oldVal) &&
          val.every((item, index) => item === oldVal[index])) {
        return;
      }
      this.setValues();
    },

    dragging(val) {
      if (!val) {
        this.setValues();
      }
    },

    firstValue(val) {
      if (this.range) {
        this.$emit('input', [this.minValue, this.maxValue]);
      } else {
        this.$emit('input', val);
      }
    },

    secondValue() {
      if (this.range) {
        this.$emit('input', [this.minValue, this.maxValue]);
      }
    },

    min() {
      this.setValues();
    },

    max() {
      this.setValues();
    }
  },

  methods: {
    valueChanged() {
      if (this.range) {
        return ![this.minValue, this.maxValue]
            .every((item, index) => item === this.oldValue[index]);
      } else {
        return this.value !== this.oldValue;
      }
    },
    setValues() {
      if (this.min > this.max) {
        console.error('[Element Error][Slider]min should not be greater than max.');
        return;
      }
      const val = this.value;
      if (this.range && Array.isArray(val)) {
        if (val[1] < this.min) {
          this.$emit('input', [this.min, this.min]);
        } else if (val[0] > this.max) {
          this.$emit('input', [this.max, this.max]);
        } else if (val[0] < this.min) {
          this.$emit('input', [this.min, val[1]]);
        } else if (val[1] > this.max) {
          this.$emit('input', [val[0], this.max]);
        } else {
          this.firstValue = val[0];
          this.secondValue = val[1];
          if (this.valueChanged()) {
            this.dispatch('ElFormItem', 'el.form.change', [this.minValue, this.maxValue]);
            this.oldValue = val.slice();
          }
        }
      } else if (!this.range && typeof val === 'number' && !isNaN(val)) {
        if (val < this.min) {
          this.$emit('input', this.min);
        } else if (val > this.max) {
          this.$emit('input', this.max);
        } else {
          this.firstValue = val;
          if (this.valueChanged()) {
            this.dispatch('ElFormItem', 'el.form.change', val);
            this.oldValue = val;
          }
        }
      }
    },

    setPosition(percent) {
      const targetValue = this.min + percent * (this.max - this.min) / 100;
      if (!this.range) {
        this.$refs.button1.setPosition(percent);
        return;
      }
      let button;
      if (Math.abs(this.minValue - targetValue) < Math.abs(this.maxValue - targetValue)) {
        button = this.firstValue < this.secondValue ? 'button1' : 'button2';
      } else {
        button = this.firstValue > this.secondValue ? 'button1' : 'button2';
      }
      this.$refs[button].setPosition(percent);
    },

    onSliderClick(event) {
      if (this.sliderDisabled || this.dragging) return;
      this.resetSize();
      if (this.vertical) {
        const sliderOffsetBottom = this.$refs.slider.getBoundingClientRect().bottom;
        this.setPosition((sliderOffsetBottom - event.clientY) / this.sliderSize * 100);
      } else {
        const sliderOffsetLeft = this.$refs.slider.getBoundingClientRect().left;
        this.setPosition((event.clientX - sliderOffsetLeft) / this.sliderSize * 100);
      }
      this.emitChange();
    },

    resetSize() {
      if (this.$refs.slider) {
        this.sliderSize = this.$refs.slider[`client${this.vertical ? 'Height' : 'Width'}`];
      }
    },

    emitChange() {
      this.$nextTick(() => {
        this.$emit('change', this.range ? [this.minValue, this.maxValue] : this.value);
      });
    },

    getStopStyle(position) {
      return this.vertical ? {'bottom': position + '%'} : {'left': position + '%'};
    }
  },

  computed: {
    stops() {
      if (!this.showStops || this.min > this.max) return [];
      if (this.step === 0) {
        process.env.NODE_ENV !== 'production' &&
        console.warn('[Element Warn][Slider]step should not be 0.');
        return [];
      }
      const stopCount = (this.max - this.min) / this.step;
      const stepWidth = 100 * this.step / (this.max - this.min);
      const result = [];
      for (let i = 1; i < stopCount; i++) {
        result.push(i * stepWidth);
      }
      if (this.range) {
        return result.filter(step => {
          return step < 100 * (this.minValue - this.min) / (this.max - this.min) ||
              step > 100 * (this.maxValue - this.min) / (this.max - this.min);
        });
      } else {
        return result.filter(step => step > 100 * (this.firstValue - this.min) / (this.max - this.min));
      }
    },

    markList() {
      if (!this.marks) {
        return [];
      }

      const marksKeys = Object.keys(this.marks);
      return marksKeys.map(parseFloat)
          .sort((a, b) => a - b)
          .filter(point => point <= this.max && point >= this.min)
          .map(point => ({
            point,
            position: (point - this.min) * 100 / (this.max - this.min),
            mark: this.marks[point]
          }));
    },

    minValue() {
      return Math.min(this.firstValue, this.secondValue);
    },

    maxValue() {
      return Math.max(this.firstValue, this.secondValue);
    },

    barSize() {
      return this.range
          ? `${100 * (this.maxValue - this.minValue) / (this.max - this.min)}%`
          : `${100 * (this.firstValue - this.min) / (this.max - this.min)}%`;
    },

    barStart() {
      return this.range
          ? `${100 * (this.minValue - this.min) / (this.max - this.min)}%`
          : '0%';
    },

    precision() {
      let precisions = [this.min, this.max, this.step].map(item => {
        let decimal = ('' + item).split('.')[1];
        return decimal ? decimal.length : 0;
      });
      return Math.max.apply(null, precisions);
    },

    runwayStyle() {
      return this.vertical ? {height: this.height} : {};
    },

    barStyle() {
      return this.vertical
          ? {
            height: this.barSize,
            bottom: this.barStart
          } : {
            width: this.barSize,
            left: this.barStart
          };
    },

    sliderDisabled() {
      return this.disabled || (this.elForm || {}).disabled;
    }
  },

  mounted() {
    let valuetext;
    if (this.range) {
      if (Array.isArray(this.value)) {
        this.firstValue = Math.max(this.min, this.value[0]);
        this.secondValue = Math.min(this.max, this.value[1]);
      } else {
        this.firstValue = this.min;
        this.secondValue = this.max;
      }
      this.oldValue = [this.firstValue, this.secondValue];
      valuetext = `${this.firstValue}-${this.secondValue}`;
    } else {
      if (typeof this.value !== 'number' || isNaN(this.value)) {
        this.firstValue = this.min;
      } else {
        this.firstValue = Math.min(this.max, Math.max(this.min, this.value));
      }
      this.oldValue = this.firstValue;
      valuetext = this.firstValue;
    }
    this.$el.setAttribute('aria-valuetext', valuetext);

    // label screen reader
    this.$el.setAttribute('aria-label', this.label ? this.label : `slider between ${this.min} and ${this.max}`);

    this.resetSize();
    window.addEventListener('resize', this.resetSize);
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.resetSize);
  }
};
</script>
