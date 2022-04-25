// 时间
let time = new Vue({
    el: "#time",
    data: {
        button: "显示时间",
        label: "2022-01-01 00:00:00",
        isShow: false,
    },
    methods: {
        showTime: function () {
            if (this.isShow === false) {
                this.button = "隐藏时间";
                this.changeTime();
            } else {
                this.button = "显示时间";
            }
            this.isShow = !this.isShow;
        },
        changeTime: function () {
            let date = new Date(); //日期对象
            let now;
            now = date.getFullYear() + "-";
            now = now + (date.getMonth() + 1) + "-";
            now = now + date.getDate() + " ";
            now = now + date.getHours() + ":";
            now = now + date.getMinutes() + ":";
            now = now + date.getSeconds();
            this.label = now;
            setInterval(this.changeTime, 1000);
        }
    }


});
// 天气的异步请求
let weather = new Vue({
    el: "#weather",
    data: {
        city: "北京",
        forecast: [],
        ganmao: "",
        wendu: "",
        yesterday: [],
    },
    methods: {
        getWeather: function () {
            let that = this;
            axios.get("http://wthrcdn.etouch.cn/weather_mini?city=" + this.city).then(
                function (res) {
                    let weather = res.data.data
                    that.forecast = weather.forecast
                    that.ganmao = weather.ganmao
                    that.wendu = weather.wendu
                    that.yesterday = weather.yesterday
                },
                function (err) {
                    console.log(err)
                }
            )
        }
    },
})
// 笑话的异步请求
let joke = new Vue({
    el: "#jokeAxios",
    data: {
        joke: "几年前去丽江玩，住宿客栈的老板是当地人，养了两只土狗。我问老板两个狗狗叫什么？老板回答：“这只是Money，" +
            "那只是Lucky。”我惊了个呆，心想这老板怎么不按套路出牌，便问道：“那么高端，还取英文名啊？”“就是旺财和来福。”",
    },
    methods: {
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
    },
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