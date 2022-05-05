new Vue({
    el: "#disk",
    created() {
        this.initData()
    },
    data() {
        return {
            userData: {
                username: "未登录",
                grade: "0",
            },
            fileList: [],
            uploadFileList: [],
            checked: false,
            search: '',
            // 自己的数据
            url: basePath + '/disk',
            business: {
                initData: "201",
                downloadFile: "202",
                updateFileList: "203",
                deleteFile: "204",
                lookupBin: "205",
                clearBin: "206",
                clearUserName: "207",
            },
            fileDir: '/',
        }
    },
    methods: {
        submitUpload() {
            this.$refs.upload.submit();
            setTimeout(() => {
                this.updateFileList();
            }, 1000)
        },

        handleRemove(file, fileList) {
            console.log(file, fileList);
        },

        handlePreview(file) {
            console.log(file);
        },

        //  自己的函数
        initData: function () {
            let data = "business=" + this.business.initData;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    let item = that.getData(str);
                    that.userData.username = item.username;
                    that.userData.grade = item.grade;
                    that.fileList = item.fileList;
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
        },

        updateFileList() {
            let data = "business=" + this.business.updateFileList + "&fileDir=" + this.fileDir;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    console.log(str)
                    let item = that.getData(str);
                    that.fileList = item.fileList;
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
        },

        openBin() {
            let data = "business=" + this.business.lookupBin;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    console.log(str)
                    let item = that.getData(str);
                    that.fileList = item.fileList;
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
        },

        Edit(index) {
            console.log(index);
        },

        Download(index) {
            let data = "business=" + this.business.downloadFile + "&fileDir=" + this.fileDir + "&fileName=" + this.fileList[index].fileName;
            let that = this
            axios.post(this.url, data, {responseType: 'blob'})
                .then(function (res) {
                    const blob = new Blob([res.data])
                    const blobUrl = window.URL.createObjectURL(blob)
                    const a = document.createElement('a')
                    a.download = that.fileList[index].fileName
                    a.href = blobUrl
                    a.click()
                    document.body.removeChild(a)
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
        },

        Delete(index) {
            let data = "business=" + this.business.deleteFile + "&fileDir=" + this.fileDir + "&fileName=" + this.fileList[index].fileName;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
            setTimeout(() => {
                this.updateFileList();
            }, 1000)
        },

        // 自己用的函数
        getData(str) {
            let item = {
                username: '',
                grade: '',
                fileList: [],
            }
            const paramList = str.split('&')
            for (let i = 0; i < paramList.length; ++i) {
                switch (paramList[i].split('=')[0]) {
                    case "username":
                        item.username = paramList[i].split('=')[1]
                        break;
                    case "grade":
                        item.grade = paramList[i].split('=')[1]
                        break;
                    case "file":
                        let file = {
                            fileName: '',
                            fileSize: '',
                            createTime: '2022-5-5'
                        }
                        file.fileName = paramList[i].split('=')[1].split(',')[0]
                        file.fileSize = paramList[i].split('=')[1].split(',')[1].split('|')[0] + "M"
                        item.fileList.push(file)
                        break;
                }
            }
            return item;
        }

    }
})

