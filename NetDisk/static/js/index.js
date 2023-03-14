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
            bg: "./static/img/index/wallpaper2.jpg",
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
                        location.href = "../../diskIndex.html";
                    }, 500)
                    break;
                case 3:
                    setTimeout(() => {
                        location.href = "../../gameIndex.html";
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
        // 是否登录
        isLogin: function () {
            let that = this;
            axios.get(userAPI + "/isLogin").then()
                .then(function (res) {
                    if (res.data.code === Has_Login.code) {
                        that.$notify({
                            type: 'success',
                            message: res.data.msg
                        });
                        that.isLoginBoolean = true
                    } else if (res.data.code === Not_Login.code) {
                        that.$notify({
                            type: 'info',
                            message: res.data.msg
                        });
                        that.isLoginBoolean = false
                    } else {
                        that.$notify({
                            type: 'error',
                            message: res.data.msg
                        });
                        that.isLoginBoolean = false
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: err.message
                    });
                });
        },
        // 退出登录
        logout: function () {
            let that = this;
            axios.get(userAPI + "/logout")
                .then(function (res) {
                    if (res.data.code === Logout_Success.code) {
                        that.$notify({
                            type: 'success',
                            message: res.data.msg
                        });
                        that.isLoginBoolean = false
                    } else {
                        that.$notify({
                            type: 'error',
                            message: res.data.msg
                        });
                        that.isLoginBoolean = true
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


