<template>
  <table class="el-year-table" @click="handleYearTableClick">
    <tbody>
    <tr>
      <td :class="getCellStyle(startYear + 0)" class="available">
        <a class="cell">{{ startYear }}</a>
      </td>
      <td :class="getCellStyle(startYear + 1)" class="available">
        <a class="cell">{{ startYear + 1 }}</a>
      </td>
      <td :class="getCellStyle(startYear + 2)" class="available">
        <a class="cell">{{ startYear + 2 }}</a>
      </td>
      <td :class="getCellStyle(startYear + 3)" class="available">
        <a class="cell">{{ startYear + 3 }}</a>
      </td>
    </tr>
    <tr>
      <td :class="getCellStyle(startYear + 4)" class="available">
        <a class="cell">{{ startYear + 4 }}</a>
      </td>
      <td :class="getCellStyle(startYear + 5)" class="available">
        <a class="cell">{{ startYear + 5 }}</a>
      </td>
      <td :class="getCellStyle(startYear + 6)" class="available">
        <a class="cell">{{ startYear + 6 }}</a>
      </td>
      <td :class="getCellStyle(startYear + 7)" class="available">
        <a class="cell">{{ startYear + 7 }}</a>
      </td>
    </tr>
    <tr>
      <td :class="getCellStyle(startYear + 8)" class="available">
        <a class="cell">{{ startYear + 8 }}</a>
      </td>
      <td :class="getCellStyle(startYear + 9)" class="available">
        <a class="cell">{{ startYear + 9 }}</a>
      </td>
      <td></td>
      <td></td>
    </tr>
    </tbody>
  </table>
</template>

<script type="text/babel">
import {hasClass} from 'element-ui/src/utils/dom';
import {getDayCountOfYear, isDate, nextDate, range} from 'element-ui/src/utils/date-util';
import {arrayFindIndex, coerceTruthyValueToArray} from 'element-ui/src/utils/util';

const datesInYear = year => {
  const numOfDays = getDayCountOfYear(year);
  const firstDay = new Date(year, 0, 1);
  return range(numOfDays).map(n => nextDate(firstDay, n));
};

export default {
  props: {
    disabledDate: {},
    value: {},
    defaultValue: {
      validator(val) {
        // null or valid Date Object
        return val === null || (val instanceof Date && isDate(val));
      }
    },
    date: {}
  },

  computed: {
    startYear() {
      return Math.floor(this.date.getFullYear() / 10) * 10;
    }
  },

  methods: {
    getCellStyle(year) {
      const style = {};
      const today = new Date();

      style.disabled = typeof this.disabledDate === 'function'
          ? datesInYear(year).every(this.disabledDate)
          : false;
      style.current = arrayFindIndex(coerceTruthyValueToArray(this.value), date => date.getFullYear() === year) >= 0;
      style.today = today.getFullYear() === year;
      style.default = this.defaultValue && this.defaultValue.getFullYear() === year;

      return style;
    },

    handleYearTableClick(event) {
      const target = event.target;
      if (target.tagName === 'A') {
        if (hasClass(target.parentNode, 'disabled')) return;
        const year = target.textContent || target.innerText;
        this.$emit('pick', Number(year));
      }
    }
  }
};
</script>
