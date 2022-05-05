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
        url: basePath + '/register',
        business: {
            checkEmail: "101",
            getVerifyCode: "102",
            register: "103",
        }
    },
    methods: {
        checkUsername: function () {
            if (this.username.length < 1) {
                console.log("用户名太短");
            } else if (this.username.length > 7) {
                console.log("用户名太长");
            } else {
                console.log("您的用户名符合要求");
            }
        },
        checkPassword: function () {
            if (this.password.length < 10) {
                console.log("密码太短");
            } else if (this.password.length > 30) {
                console.log("密码太长");
            } else {
                console.log("您的密码符合要求");
            }
        },
        checkEmail: function () {
            let data = "business=" + this.business.checkEmail + "&email=" + this.email;
            console.log(data);
            axios.post(this.url, data)
                .then(function (res) {
                    console.log(res.data);
                    if (res.data == "1") {
                        console.log("邮箱可以使用");
                    } else {
                        console.log("邮箱已被注册");
                    }
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
        },
        checkConPassword: function () {
            if (this.conPassword !== this.password) {
                console.log("两次密码不一致");
            } else {
                console.log("两次密码一致");
            }

        },
        getVerifyCode: function () {
            let data = "business=" + this.business.getVerifyCode + "&email=" + this.email;
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        console.log("验证码发送成功");
                    } else {
                        console.log("验证码发送失败");
                    }
                })
                .catch(function (err) {
                    console.log("访问后台失败");
                });
        },
        register: function () {
            if (this.username == "") {
                console.log("请输入用户名");
            } else if (this.password == "") {
                console.log("请输入密码");
            } else if (this.email == "") {
                console.log("请输入邮箱");
            } else if (this.conPassword == "") {
                console.log("请再次输入密码");
            } else if (this.verifyCode == "") {
                console.log("请输入验证码");
            } else {
                let data = "business=" + this.business.register + "&username=" + this.username + "&password=" + this.password + "&email=" + this.email + "&verifyCode=" + this.verifyCode;
                axios.post(this.url, data)
                    .then(function (res) {
                        if (res.data == "1") {
                            console.log("注册成功");
                            // 注册成功自动点击 登录
                            setTimeout(() => {
                                login.click();
                            }, 1000)
                        } else {
                            console.log("注册失败");
                        }
                    })
                    .catch(function (err) {
                        console.log("访问后台失败");
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
        url: basePath + "/register",
        business: {
            login: "104",
        }
    },
    methods: {
        checkEmail: function () {
            if (this.email.indexOf('@') == -1) {
                console.log("请输入正确的邮箱");
            } else {
                console.log("您的邮箱符合要求");
            }
        },
        checkPassword: function () {
            if (this.password.length < 10) {
                console.log("密码太短");
            } else if (this.password.length > 30) {
                console.log("密码太长");
            } else {
                console.log("您的密码符合要求");
            }
        },
        login: function () {
            if (this.email == "") {
                console.log("请输入邮箱");
            } else if (this.password == "") {
                console.log("请输入密码");
            } else {
                let data = "business=" + this.business.login + "&email=" + this.email + "&password=" + this.password;
                axios.post(this.url, data)
                    .then(function (res) {
                        if (res.data == "1") {
                            console.log("成功了");
                            // 登陆成功 跳转主页
                            setTimeout(() => {
                                location.href = "index.html";
                            }, 1000)
                        } else {
                            alert("账号或密码错误");
                        }
                    })
                    .catch(function (err) {
                        console.log("访问后台失败");
                    });
            }
        },
    }
});


