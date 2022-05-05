new Vue({
    el: '#app',
    created() {
        this.updateTime();
        this.getWeather();
        this.getJoke();
    },

    data: function () {
        return {
            drawer: false,
            visible: false,
            value: new Date(),
            activeName: 'second',
            // 我自己的数据
            time: '00:00',
            date: '2022-01-01',
            city: "天津",
            forecast: [],
            joke: "",
            url: basePath + "/login",
            business: {
                logout: "109",
            }
        }
    },

    methods: {
        handleClick: function (tab, event) {
            console.log(tab, event);
        },

        // 我自己的方法
        updateTime: function () {
            let now = new Date(); //日期对象
            let map = {1: "一", 2: "二", 3: "三", 4: "四", 5: "五", 6: "六", 7: "日"}
            this.time = now.getHours() + ":" + now.getMinutes();
            this.date = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate() +
                "   星期" + map[now.getDay()];
            setInterval(this.updateTime, 1000);
        },
        modifyCity: function (city) {
            // TODO : 添加 修改城市
            this.city = city;
        },
        getWeather: function () {
            let that = this;
            axios.get("http://wthrcdn.etouch.cn/weather_mini?city=" + this.city).then(
                function (res) {
                    let weather = res.data.data
                    that.forecast = weather.forecast[0]
                },
                function (err) {
                    console.log(err)
                }
            )
        },
        getJoke: function () {
            let that = this;
            axios.get("https://autumnfish.cn/api/joke").then(
                function (response) {
                    that.joke = response.data
                }, function (error) {
                    console.log(error)
                }
            );
        },
        logout: function () {
            let data = "business=" + this.business.logout;
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        console.log("成功了");
                    } else {
                        console.log("失败了");
                    }
                })
                .catch(function (err) {
                    console.log("访问后台失败");
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


