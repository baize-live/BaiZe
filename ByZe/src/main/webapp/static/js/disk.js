new Vue({
    el: "#disk",
    created() {
        this.initData()
        this.getFriend()
    },
    data() {
        return {
            userData: {
                uid: '',
                username: "未登录",
                grade: "0",
                icon: '',
                nowStorage: 0,
                maxStorage: 1024,
                email: "",
                phone: "",
                idCard: "",
                realName: "",
            },
            fileList: [],
            binList: [],
            friendList: [],
            uploadFileList: [],
            checked: false,
            search: '',
            isCollapse: false,
            // 自己的数据
            url: basePath + '/disk',
            business: {
                initData: "201",
                downloadFile: "202",
                updateFileList: "203",
                deleteFile: "204",
                lookupBin: "205",
                clearBin: "206",
                clearUserFile: "207",
                recoveryFile: "208",
                updateAttributes: "209",
                friend: "210",
                modifyAttributes: "211",
            },
            fileDir: '/',
            isShow: [true, false, false, false],
        }
    },
    methods: {
        submitUpload() {
            this.$refs.upload.submit();
            this.$notify({
                type: 'success',
                message: '上传成功'
            });
            setTimeout(() => {
                this.updateFileList()
            }, 1000)
            this.$refs.upload.clearFiles();
        },

        handleRemove(file, fileList) {
            console.log(file, fileList);
        },

        handlePreview(file) {
            console.log(file);
        },

        handleSelect(key, keyPath) {
            if (key == 0) {
                this.updateFileList();
            } else if (key == 1) {
                this.openBin();
            } else if (key == 2) {
                this.getFriend();
            } else if (key == 3) {
                this.updateAttributes()
            }
            for (let i = 0; i < this.isShow.length; ++i) {
                if (i == key) {
                    this.isShow[i] = true;
                } else {
                    this.isShow[i] = false;
                }
            }
        },
        //  自己的函数
        initData() {
            let data = "business=" + this.business.initData;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    let item = that.getData(str);
                    that.userData = item.userData;
                    that.fileList = item.fileList;
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },

        updateFileList() {
            let data = "business=" + this.business.updateFileList + "&fileDir=" + this.fileDir;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    let item = that.getData(str);
                    that.fileList = item.fileList;
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },

        updateAttributes() {
            let data = "business=" + this.business.updateAttributes;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    console.log(str)
                    let item = that.getData(str);
                    that.userData = item.userData
                    console.log(that.userData)

                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },

        modifyAttributes() {
            let data = "business=" + this.business.modifyAttributes
                + "&idCard=" + this.userData.idCard
                + "&realName=" + this.userData.realName
                + "&phone=" + this.userData.phone
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '修改成功'
                        });
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },

        openBin() {
            let data = "business=" + this.business.lookupBin;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    let item = that.getData(str);
                    that.binList = item.fileList;
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
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
                    that.$notify({
                        type: 'success',
                        message: '开始下载'
                    });
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },

        Delete(index) {
            let data = "business=" + this.business.deleteFile + "&fileDir=" + this.fileDir + "&fileName=" + this.fileList[index].fileName;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '删除成功'
                        });
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
            setTimeout(() => {
                this.updateFileList();
            }, 1000)
        },

        getFriend() {
            let data = "business=" + this.business.friend;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    let item = that.parseFriend(str);
                    that.friendList = item.friendList;
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
        },

        recovery(index) {
            let data = "business=" + this.business.recoveryFile + "&fileDir=" + this.fileDir + "&fileName=" + this.binList[index].fileName;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '恢复成功'
                        });
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
            setTimeout(() => {
                this.openBin();
            }, 1000)
        },

        clear(index) {
            let data = "business=" + this.business.clearUserFile + "&fileDir=" + this.fileDir + "&fileName=" + this.binList[index].fileName;
            let that = this
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '清除成功'
                        });
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
            setTimeout(() => {
                this.openBin();
            }, 1000)
        },

        clearBin() {
            let data = "business=" + this.business.clearBin;
            let that = this
            axios.post(this.url, data)
                .then(function (res) {
                    if (res.data == "1") {
                        that.$notify({
                            type: 'success',
                            message: '清空成功'
                        });
                    }
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: '网络异常'
                    });
                });
            setTimeout(() => {
                this.openBin();
            }, 1000)
        },

        // 自己用的函数
        getData(str) {
            let item = {
                userData: {
                    uid: "",
                    username: "未登录",
                    grade: "0",
                    icon: '',
                    nowStorage: 0,
                    maxStorage: 1024,
                    email: "",
                    phone: "",
                    idCard: "",
                    realName: "",
                },
                fileList: [],
            }
            const paramList = str.split('&')
            for (let i = 0; i < paramList.length; ++i) {
                switch (paramList[i].split('=')[0]) {
                    case "uid":
                        item.userData.uid = paramList[i].split('=')[1]
                        break;
                    case "username":
                        item.userData.username = paramList[i].split('=')[1]
                        break;
                    case "grade":
                        item.userData.grade = paramList[i].split('=')[1]
                        break;
                    case "icon":
                        item.userData.icon = "static/img/pandataIcon/" + paramList[i].split('=')[1]
                        break;
                    case "nowStorage":
                        item.userData.nowStorage = parseInt(paramList[i].split('=')[1])
                        break;
                    case "maxStorage":
                        item.userData.maxStorage = parseInt(paramList[i].split('=')[1])
                        break;
                    case "email":
                        item.userData.email = paramList[i].split('=')[1]
                        break;
                    case "phone":
                        item.userData.phone = paramList[i].split('=')[1]
                        break;
                    case "idCard":
                        item.userData.idCard = paramList[i].split('=')[1]
                        break;
                    case "realName":
                        item.userData.realName = paramList[i].split('=')[1]
                        break;
                    case "file":
                        let file = {
                            fileName: '',
                            fileSize: '',
                            Time: '2022-5-5'
                        }
                        file.fileName = paramList[i].split('=')[1].split(',')[0]
                        file.fileSize = paramList[i].split('=')[1].split(',')[1].split('|')[0] + "M"
                        item.fileList.push(file)
                        break;
                }
            }
            // 按ASCII 排序
            item.fileList.sort((a, b) => a.fileName < b.fileName ? -1 : 1)
            return item;
        },

        parseFriend(str) {
            let item = {
                friendList: [],
            }
            const paramList = str.split('&')
            for (let i = 0; i < paramList.length; ++i) {
                let friend = {
                    username: '',
                    group: '',
                }
                friend.username = paramList[i].split(',')[0]
                friend.group = paramList[i].split(',')[1]
                item.friendList.push(friend)
            }
            return item;
        }
    }
})

