new Vue({
    el: "#diskIndex",
    data: {
        email: "",
        password: "",
        bg: "./static/img/index/wallpaper2.jpg",
    },
    methods: {
        enterPan: function () {
            let that = this
            axios.get(userAPI_hasLogin + "/isOpenDisk")
                .then(function (res) {
                    if (res.data.code === Has_OpenDisk.code) {
                        setTimeout(() => {
                            location.href = "disk.html";
                        }, 1000)
                    } else if (res.data.code === Not_OpenDisk.code) {
                        // 弹出对话框
                        that.$confirm('网盘暂未开通', '是否开通?', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            that.openPan();
                        }).catch(() => {
                            that.$notify({
                                type: 'info',
                                message: '已取消'
                            });
                        });
                    } else if (res.data.code === Not_Login.code) {
                        that.$notify({
                            type: 'info',
                            message: '请先登录'
                        });
                        setTimeout(() => {
                            location.href = "login.html";
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
        },
        openPan: function () {
            let that = this
            axios.get(userAPI_hasLogin + "/openDisk")
                .then(function (res) {
                    if (res.data.code === OpenDisk_Success.code) {
                        that.$notify({
                            type: 'success',
                            message: res.data.msg
                        });
                        that.enterPan()
                    } else if (res.data.code === OpenDisk_Failure.code) {
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
                        message: err.message
                    });
                });
        },
    }
});
