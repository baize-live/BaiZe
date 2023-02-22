// login.js

// 实现页面效果
let login = document.getElementById('goLogin');
let register = document.getElementById('goRegister');
let form_box = document.getElementsByClassName('form-box')[0];
let register_box = document.getElementsByClassName('register-box')[0];
let login_box = document.getElementsByClassName('login-box')[0];

register.addEventListener("click", () => {
    form_box.style.transform = "translateX(80%)";
    login_box.classList.add("hidden");
    register_box.classList.remove("hidden");
})
login.addEventListener("click", () => {
    form_box.style.transform = "translateX(0%)";
    register_box.classList.add("hidden");
    login_box.classList.remove("hidden");
})


// 注册逻辑
new Vue({
    el: "#register",
    data: {
        username: "",
        password: "",
        email: "",
        conPassword: "",
        verifyCode: "",
        trueVerifyCode: "",
    },
    methods: {
        checkUsername: function () {
            if (this.username.length < 1) {
                this.$notify({
                    type: 'info',
                    message: '用户名太短'
                });
            } else if (this.username.length > 7) {
                this.$notify({
                    type: 'info',
                    message: '用户名太长'
                });
            } else {
                this.$notify({
                    type: 'info',
                    message: '您的用户名符合要求'
                });
            }
        },
        checkPassword: function () {
            if (this.password.length < 10) {
                this.$notify({
                    type: 'info',
                    message: '密码太短'
                });
            } else if (this.password.length > 30) {
                this.$notify({
                    type: 'info',
                    message: '密码太长'
                });
            } else {
                this.$notify({
                    type: 'info',
                    message: '您的密码符合要求'
                });
            }
        },
        checkConPassword: function () {
            if (this.conPassword !== this.password) {
                this.$notify({
                    type: 'info',
                    message: '两次密码不一致'
                });
            } else {
                this.$notify({
                    type: 'info',
                    message: '两次密码一致'
                });
            }
        },
        checkEmail: function () {
            if (this.email === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入邮箱'
                });
            } else if (this.email.indexOf('@') === -1) {
                this.$notify({
                    type: 'info',
                    message: '请输入正确的邮箱'
                });
            } else {
                let that = this
                axios.get(userAPI + "/checkEmail?email=" + this.email)
                    .then(function (res) {
                        if (res.data.code === Has_Email.code || res.data.code === Not_Email.code) {
                            that.$notify({
                                type: 'info',
                                message: res.data.msg
                            });
                        } else {
                            that.$notify({
                                type: 'error',
                                message: res.data.msg
                            });
                        }
                    })
                    .catch(function (err) {
                        that.$notify({
                            type: 'error',
                            message: '未知异常' + err.message
                        });
                    });
            }
        },
        getVerifyCode: function () {
            if (this.email.indexOf('@') === -1) {
                this.$notify({
                    type: 'info',
                    message: '请输入正确的邮箱'
                });
            } else {
                if (this.email.length <= 1) {
                    this.$notify({
                        type: 'info',
                        message: '请输入正确的邮箱'
                    });
                } else {
                    let that = this
                    axios.get(userAPI + "/sendVerifyCode?email=" + this.email)
                        .then(function (res) {
                            if (res.data.code === VerifyCode_Send_Success.code) {
                                that.$notify({
                                    type: 'success',
                                    message: res.data.msg
                                });
                            } else {
                                that.$notify({
                                    type: 'error',
                                    message: "未知异常"
                                });
                            }

                        })
                        .catch(function (err) {
                            that.$notify({
                                type: 'error',
                                message: err.message
                            });
                        });
                }
            }
        },
        register: function () {
            if (this.username === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入用户名'
                });
            } else if (this.password === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入密码'
                });
            } else if (this.email === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入邮箱'
                });
            } else if (this.conPassword === "") {
                this.$notify({
                    type: 'info',
                    message: '请再次输入密码'
                });
            } else if (this.verifyCode === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入验证码'
                });
            } else {
                let that = this
                axios.get(userAPI + "/register?username=" + this.username + "&password=" + this.password + "&email=" + this.email + "&verifyCode=" + this.verifyCode)
                    .then(function (res) {
                        if (res.data.code === Register_Success.code) {
                            that.$notify({
                                type: 'success',
                                message: res.data.msg
                            });
                            // 注册成功
                            setTimeout(() => {
                                login.click()
                            }, 1000)
                        } else {
                            that.$notify({
                                type: 'error',
                                message: res.data.msg
                            });
                        }
                    })
                    .catch(function (err) {
                        that.$notify({
                            type: 'error',
                            message: err.message
                        });
                    });
            }
        },
    }
});

// 登录逻辑
new Vue({
    el: "#login",
    data: {
        email: "",
        password: "",
    },
    methods: {
        checkEmail: function () {
            if (this.email.indexOf('@') === -1) {
                this.$notify({
                    type: 'info',
                    message: '请输入正确的邮箱'
                });
            } else {
                this.$notify({
                    type: 'info',
                    message: '您的邮箱符合要求'
                });
            }
        },

        checkPassword: function () {
            if (this.password.length < 10) {
                this.$notify({
                    type: 'info',
                    message: '密码太短'
                });
            } else if (this.password.length > 30) {
                this.$notify({
                    type: 'info',
                    message: '密码太长'
                });
            } else {
                this.$notify({
                    type: 'info',
                    message: '您的密码符合要求'
                });
            }
        },

        login: function () {
            if (this.email === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入邮箱'
                });
            } else if (this.password === "") {
                this.$notify({
                    type: 'info',
                    message: '请输入密码'
                });
            } else {
                let that = this
                axios.get(userAPI + "/login?email=" + this.email + "&password=" + this.password)
                    .then(function (res) {
                        if (res.data.code === Login_Success.code) {
                            that.$notify({
                                type: 'success',
                                message: res.data.msg
                            });
                            // 登陆成功 跳转主页
                            setTimeout(() => {
                                location.href = "./index.html";
                            }, 1000)
                        } else {
                            that.$notify({
                                type: 'error',
                                message: res.data.msg
                            });
                        }
                    })
                    .catch(function (err) {
                        that.$notify({
                            type: 'error',
                            message: err.message
                        });
                    });
            }
        },
    }
});

