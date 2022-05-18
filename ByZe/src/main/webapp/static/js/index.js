new Vue({
    el: '#app',
    created() {
        this.isLogin();
        this.getQuotes();
    },

    data: function () {
        return {
            drawer: false,
            visible: false,
            value: new Date(),
            activeName: 'second',
            // 我自己的数据
            isLoginBoolean: false,
            Quotes: "",
            url: basePath,
            business: {
                logoutString: "109",
                isLoginString: "110",
            },
            bg: "./static/img/index/wallpaper2.png",
        }
    },

    methods: {
        handleClick: function (tab, event) {
            console.log(tab, event);
        },
        handleSelect(key) {

            switch (key) {
                case 2:
                    setTimeout(() => {
                        location.href = "./pan.html";
                    }, 500)
                    break;
                case 3:
                    setTimeout(() => {
                        location.href = "./you.html";
                    }, 500)
                    break;
                case 4:
                    setTimeout(() => {
                        location.href = "./person.html";
                    }, 500)
                    break;
                case 5:
                    setTimeout(() => {
                        location.href = "./login.html";
                    }, 500)
                    break;
                case 6:
                    this.logout();
                    break;
            }
        },
        // 我自己的方法
        getQuotes: function () {
            let that = this;
            axios.get("https://api.xygeng.cn/one").then(
                function (response) {
                    that.Quotes = response.data.data.content
                }, function (error) {
                    console.log(error)
                }
            );
        },
        // 判断是否登录
        isLogin: function () {
            let data = "business=" + this.business.isLoginString;
            let that = this;
            axios.post(this.url + "/register", data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '登录成功'
                        });
                        that.isLoginBoolean = true
                    } else {
                        that.$notify({
                            type: 'info',
                            message: '无用户登录'
                        });
                        that.isLoginBoolean = false
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },
        // 退出登录
        logout: function () {
            let data = "business=" + this.business.logoutString;
            let that = this;
            axios.post(this.url + "/login", data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '退出成功'
                        });
                        that.isLoginBoolean = false
                    } else {
                        that.$notify({
                            type: 'warning',
                            message: '无用户登录'
                        });
                        that.isLoginBoolean = true
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        }
    }
})

console.log(
    "░░░░░░░░░▄▄\n" +
    "░░░░░░░░░█░█\n" +
    "░░░░░░░░░█░█\n" +
    "░░░░░░░░█░░█\n" +
    "░░░░░░░█░░░█\n" +
    "█████▄▄█░░░████\n" +
    "▓▓▓▓█░░░░░░░░░░░░█\n" +
    "▓▓▓▓█░░░░░░░░░░░░█\n" +
    "▓▓▓▓█░░░░░░░░░░░░█\n" +
    "▓▓▓▓█░░░░░░░░░░░░█\n" +
    "▓▓▓▓█░░░░░░░░░░░░█\n" +
    "▓▓▓▓█████░░░░░░░░\n" +
    "████▀░░░▀▀██████▀\n"
)


