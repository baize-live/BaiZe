<template>
  <transition name="msgbox-fade">
    <div
        v-show="visible"
        :aria-label="title || 'dialog'"
        aria-modal="true"
        class="el-message-box__wrapper"
        role="dialog"
        tabindex="-1"
        @click.self="handleWrapperClick">
      <div :class="[customClass, center && 'el-message-box--center']" class="el-message-box">
        <div v-if="title !== null" class="el-message-box__header">
          <div class="el-message-box__title">
            <div
                v-if="icon && center"
                :class="['el-message-box__status', icon]">
            </div>
            <span>{{ title }}</span>
          </div>
          <button
              v-if="showClose"
              aria-label="Close"
              class="el-message-box__headerbtn"
              type="button"
              @click="handleAction(distinguishCancelAndClose ? 'close' : 'cancel')"
              @keydown.enter="handleAction(distinguishCancelAndClose ? 'close' : 'cancel')">
            <i class="el-message-box__close el-icon-close"></i>
          </button>
        </div>
        <div class="el-message-box__content">
          <div class="el-message-box__container">
            <div
                v-if="icon && !center && message !== ''"
                :class="['el-message-box__status', icon]">
            </div>
            <div v-if="message !== ''" class="el-message-box__message">
              <slot>
                <p v-if="!dangerouslyUseHTMLString">{{ message }}</p>
                <p v-else v-html="message"></p>
              </slot>
            </div>
          </div>
          <div v-show="showInput" class="el-message-box__input">
            <el-input
                ref="input"
                v-model="inputValue"
                :placeholder="inputPlaceholder"
                :type="inputType"
                @keydown.enter.native="handleInputEnter"></el-input>
            <div :style="{ visibility: !!editorErrorMessage ? 'visible' : 'hidden' }" class="el-message-box__errormsg">
              {{ editorErrorMessage }}
            </div>
          </div>
        </div>
        <div class="el-message-box__btns">
          <el-button
              v-if="showCancelButton"
              :class="[ cancelButtonClasses ]"
              :loading="cancelButtonLoading"
              :round="roundButton"
              size="small"
              @click.native="handleAction('cancel')"
              @keydown.enter="handleAction('cancel')">
            {{ cancelButtonText || t('el.messagebox.cancel') }}
          </el-button>
          <el-button
              v-show="showConfirmButton"
              ref="confirm"
              :class="[ confirmButtonClasses ]"
              :loading="confirmButtonLoading"
              :round="roundButton"
              size="small"
              @click.native="handleAction('confirm')"
              @keydown.enter="handleAction('confirm')">
            {{ confirmButtonText || t('el.messagebox.confirm') }}
          </el-button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script type="text/babel">
import Popup from 'element-ui/src/utils/popup';
import Locale from 'element-ui/src/mixins/locale';
import ElInput from 'element-ui/packages/input';
import ElButton from 'element-ui/packages/button';
import {addClass, removeClass} from 'element-ui/src/utils/dom';
import {t} from 'element-ui/src/locale';
import Dialog from 'element-ui/src/utils/aria-dialog';

let messageBox;
let typeMap = {
  success: 'success',
  info: 'info',
  warning: 'warning',
  error: 'error'
};

export default {
  mixins: [Popup, Locale],

  props: {
    modal: {
      default: true
    },
    lockScroll: {
      default: true
    },
    showClose: {
      type: Boolean,
      default: true
    },
    closeOnClickModal: {
      default: true
    },
    closeOnPressEscape: {
      default: true
    },
    closeOnHashChange: {
      default: true
    },
    center: {
      default: false,
      type: Boolean
    },
    roundButton: {
      default: false,
      type: Boolean
    }
  },

  components: {
    ElInput,
    ElButton
  },

  computed: {
    icon() {
      const {type, iconClass} = this;
      return iconClass || (type && typeMap[type] ? `el-icon-${typeMap[type]}` : '');
    },

    confirmButtonClasses() {
      return `el-button--primary ${this.confirmButtonClass}`;
    },
    cancelButtonClasses() {
      return `${this.cancelButtonClass}`;
    }
  },

  methods: {
    getSafeClose() {
      const currentId = this.uid;
      return () => {
        this.$nextTick(() => {
          if (currentId === this.uid) this.doClose();
        });
      };
    },
    doClose() {
      if (!this.visible) return;
      this.visible = false;
      this._closing = true;

      this.onClose && this.onClose();
      messageBox.closeDialog(); // 解绑
      if (this.lockScroll) {
        setTimeout(this.restoreBodyStyle, 200);
      }
      this.opened = false;
      this.doAfterClose();
      setTimeout(() => {
        if (this.action) this.callback(this.action, this);
      });
    },

    handleWrapperClick() {
      if (this.closeOnClickModal) {
        this.handleAction(this.distinguishCancelAndClose ? 'close' : 'cancel');
      }
    },

    handleInputEnter() {
      if (this.inputType !== 'textarea') {
        return this.handleAction('confirm');
      }
    },

    handleAction(action) {
      if (this.$type === 'prompt' && action === 'confirm' && !this.validate()) {
        return;
      }
      this.action = action;
      if (typeof this.beforeClose === 'function') {
        this.close = this.getSafeClose();
        this.beforeClose(action, this, this.close);
      } else {
        this.doClose();
      }
    },

    validate() {
      if (this.$type === 'prompt') {
        const inputPattern = this.inputPattern;
        if (inputPattern && !inputPattern.test(this.inputValue || '')) {
          this.editorErrorMessage = this.inputErrorMessage || t('el.messagebox.error');
          addClass(this.getInputElement(), 'invalid');
          return false;
        }
        const inputValidator = this.inputValidator;
        if (typeof inputValidator === 'function') {
          const validateResult = inputValidator(this.inputValue);
          if (validateResult === false) {
            this.editorErrorMessage = this.inputErrorMessage || t('el.messagebox.error');
            addClass(this.getInputElement(), 'invalid');
            return false;
          }
          if (typeof validateResult === 'string') {
            this.editorErrorMessage = validateResult;
            addClass(this.getInputElement(), 'invalid');
            return false;
          }
        }
      }
      this.editorErrorMessage = '';
      removeClass(this.getInputElement(), 'invalid');
      return true;
    },
    getFirstFocus() {
      const btn = this.$el.querySelector('.el-message-box__btns .el-button');
      const title = this.$el.querySelector('.el-message-box__btns .el-message-box__title');
      return btn || title;
    },
    getInputElement() {
      const inputRefs = this.$refs.input.$refs;
      return inputRefs.input || inputRefs.textarea;
    },
    handleClose() {
      this.handleAction('close');
    }
  },

  watch: {
    inputValue: {
      immediate: true,
      handler(val) {
        this.$nextTick(_ => {
          if (this.$type === 'prompt' && val !== null) {
            this.validate();
          }
        });
      }
    },

    visible(val) {
      if (val) {
        this.uid++;
        if (this.$type === 'alert' || this.$type === 'confirm') {
          this.$nextTick(() => {
            this.$refs.confirm.$el.focus();
          });
        }
        this.focusAfterClosed = document.activeElement;
        messageBox = new Dialog(this.$el, this.focusAfterClosed, this.getFirstFocus());
      }

      // prompt
      if (this.$type !== 'prompt') return;
      if (val) {
        setTimeout(() => {
          if (this.$refs.input && this.$refs.input.$el) {
            this.getInputElement().focus();
          }
        }, 500);
      } else {
        this.editorErrorMessage = '';
        removeClass(this.getInputElement(), 'invalid');
      }
    }
  },

  mounted() {
    this.$nextTick(() => {
      if (this.closeOnHashChange) {
        window.addEventListener('hashchange', this.close);
      }
    });
  },

  beforeDestroy() {
    if (this.closeOnHashChange) {
      window.removeEventListener('hashchange', this.close);
    }
    setTimeout(() => {
      messageBox.closeDialog();
    });
  },

  data() {
    return {
      uid: 1,
      title: undefined,
      message: '',
      type: '',
      iconClass: '',
      customClass: '',
      showInput: false,
      inputValue: null,
      inputPlaceholder: '',
      inputType: 'text',
      inputPattern: null,
      inputValidator: null,
      inputErrorMessage: '',
      showConfirmButton: true,
      showCancelButton: false,
      action: '',
      confirmButtonText: '',
      cancelButtonText: '',
      confirmButtonLoading: false,
      cancelButtonLoading: false,
      confirmButtonClass: '',
      confirmButtonDisabled: false,
      cancelButtonClass: '',
      editorErrorMessage: null,
      callback: null,
      dangerouslyUseHTMLString: false,
      focusAfterClosed: null,
      isOnComposition: false,
      distinguishCancelAndClose: false
    };
  }
};
</script>
