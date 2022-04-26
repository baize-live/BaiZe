// 时间
let time = new Vue({
    el: "#time",
    created() {
        this.updateTime();
    },
    data: {
        time: "00:00",
        date: "2022-01-01"
    },
    methods: {
        updateTime: function () {
            let now = new Date(); //日期对象
            let map = {1: "一", 2: "二", 3: "三", 4: "四", 5: "五", 6: "六", 7: "日"}
            this.time = now.getHours() + ":" + now.getMinutes();
            this.date = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate() +
                "   星期" + map[now.getDay()];
            setInterval(this.updateTime, 1000);
        }
    }
});

// 天气
let weather = new Vue({
    el: "#weather",
    created() {
        this.getWeather();
    },
    data: {
        city: "天津",
        forecast: [],
    },
    methods: {
        // TODO : 添加 修改城市
        modifyCity: function (city) {
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


