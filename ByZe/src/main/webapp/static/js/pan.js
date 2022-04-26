const basePath = 'http://localhost:8080/ByZe'

new Vue({
    el: "#hello",
    created() {
        this.helloWorld();
    },
    data: {
        url: basePath + "/byzepan",
        shu: "<p>nihao</p>",
        business: {
            hello: "201",
        }
    },
    methods: {
        helloWorld: function () {
            let that = this;
            let data = "business=" + this.business.hello;
            axios.post(this.url, data).then(
                function (res) {
                    console.log(res.data);
                    that.shu = res.data;
                },
                function (err) {
                    console.log(err)
                }
            )
        }
    },
})
