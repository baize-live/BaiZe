<template>
  <transition-group
      :class="[
      'el-upload-list',
      'el-upload-list--' + listType,
      { 'is-disabled': disabled }
    ]"
      name="el-list"
      tag="ul"
  >
    <li
        v-for="file in files"
        :key="file.uid"
        :class="['el-upload-list__item', 'is-' + file.status, focusing ? 'focusing' : '']"
        tabindex="0"
        @blur="focusing = false"
        @click="focusing = false"
        @focus="focusing = true"
        @keydown.delete="!disabled && $emit('remove', file)"
    >
      <slot :file="file">
        <img
            v-if="file.status !== 'uploading' && ['picture-card', 'picture'].indexOf(listType) > -1"
            :src="file.url"
            alt="" class="el-upload-list__item-thumbnail"
        >
        <a class="el-upload-list__item-name" @click="handleClick(file)">
          <i class="el-icon-document"></i>{{ file.name }}
        </a>
        <label class="el-upload-list__item-status-label">
          <i :class="{
            'el-icon-upload-success': true,
            'el-icon-circle-check': listType === 'text',
            'el-icon-check': ['picture-card', 'picture'].indexOf(listType) > -1
          }"></i>
        </label>
        <i v-if="!disabled" class="el-icon-close" @click="$emit('remove', file)"></i>
        <i v-if="!disabled" class="el-icon-close-tip">{{ t('el.upload.deleteTip') }}</i>
        <!--因为close按钮只在li:focus的时候 display, li blur后就不存在了，所以键盘导航时永远无法 focus到 close按钮上-->
        <el-progress
            v-if="file.status === 'uploading'"
            :percentage="parsePercentage(file.percentage)"
            :stroke-width="listType === 'picture-card' ? 6 : 2"
            :type="listType === 'picture-card' ? 'circle' : 'line'">
        </el-progress>
        <span v-if="listType === 'picture-card'" class="el-upload-list__item-actions">
          <span
              v-if="handlePreview && listType === 'picture-card'"
              class="el-upload-list__item-preview"
              @click="handlePreview(file)"
          >
            <i class="el-icon-zoom-in"></i>
          </span>
          <span
              v-if="!disabled"
              class="el-upload-list__item-delete"
              @click="$emit('remove', file)"
          >
            <i class="el-icon-delete"></i>
          </span>
        </span>
      </slot>
    </li>
  </transition-group>
</template>
<script>
import Locale from 'element-ui/src/mixins/locale';
import ElProgress from 'element-ui/packages/progress';

export default {

  name: 'ElUploadList',

  mixins: [Locale],

  data() {
    return {
      focusing: false
    };
  },
  components: {ElProgress},

  props: {
    files: {
      type: Array,
      default() {
        return [];
      }
    },
    disabled: {
      type: Boolean,
      default: false
    },
    handlePreview: Function,
    listType: String
  },
  methods: {
    parsePercentage(val) {
      return parseInt(val, 10);
    },
    handleClick(file) {
      this.handlePreview && this.handlePreview(file);
    }
  }
};
</script>
