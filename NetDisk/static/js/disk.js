new Vue({
    el: "#disk",
    created() {
        this.initUserData()
    },
    data() {
        return {
            userData: {
                idCard: "",
                realName: "",
                phone: "",
            },
            diskData: {
                grade: 0,
                nowStorage: 0,
                maxStorage: 0,
            },
            fileList: [],
            binList: [],
            friendList: [],
            uploadFileList: [],
            uploadUrl: diskAPI + "/uploadFile",
            checked: false,
            search: '',
            isCollapse: false,
            fileDir: '/',
            isShow: [true, false, false, false],
            selection: [],
            uploadNum: 0,
        }
    },
    methods: {
        submitUpload() {
            this.$refs.upload.submit();
        },

        handleSuccess(res, file, fileList) {
            this.uploadNum++;
            if (res.code === UploadFile_Success.code) {
                this.$notify({
                    type: 'success',
                    message: res.msg
                });
            } else {
                this.$notify({
                    type: 'error',
                    message: res.msg
                });
            }
            if (this.uploadNum === fileList.length) {
                this.uploadNum = 0;
                setTimeout(() => {
                    this.$refs.upload.clearFiles();
                    this.getUserFileList();
                }, 1000)
            }
        },

        handleError(err, file, fileList) {
            this.$notify({
                type: 'error',
                message: '上传失败, 请检查网络'
            });
        },

        handleSelect(key, keyPath) {
            if (key == 0) {
                this.getUserFileList();
            } else if (key == 1) {
                this.lookupAllFilesFromBin();
            } else if (key == 2) {
                this.getFriendList();
            } else if (key == 3) {
                this.getUserDiskData()
            }
            for (let i = 0; i < this.isShow.length; ++i) {
                this.isShow[i] = (i == key);
            }
            console.log(this.isShow)
        },

        handleSelectionChange(val) {
            console.log(val)
            this.selection = val
        },

        //  自己的函数
        initUserData() {
            this.getUserDiskData();
            this.getUserFileList();
        },

        getUserDiskData() {
            let that = this
            // 获得用户网盘数据
            axios.get(diskAPI + "/getUserDiskData")
                .then(function (res) {
                    if (res.data.code === GetUserDiskData_Success.code) {
                        that.userData = res.data.data.userData
                        that.diskData = res.data.data.diskData
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

        getUserFileList() {
            let that = this
            // 获得用户文件列表
            axios.get(diskAPI + "/getUserFileList?fileDir=" + this.fileDir)
                .then(function (res) {
                    if (res.data.code === GetUserFileList_Success.code) {
                        that.fileList = res.data.data
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

        modifyUserData() {
            let that = this
            // 修改用户属性
            axios.get(userAPI_hasLogin + "/modifyUserData?idCard=" + this.userData.idCard + "&realName=" + this.userData.realName + "&phone=" + this.userData.phone)
                .then(function (res) {
                    if (res.data.code === ModifyUserData_Success.code) {
                        that.$notify({
                            type: 'success',
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

        lookupAllFilesFromBin() {
            let that = this
            axios.get(diskAPI + "/lookupAllFilesFromBin")
                .then(function (res) {
                    if (res.data.code === LookupAllFilesFromBin_Success.code) {
                        that.binList = res.data.data
                        that.$notify({
                            type: 'info',
                            message: res.data.msg
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

        getFriendList() {
            let that = this
            axios.get(diskAPI + "/getFriendList")
                .then(function (res) {
                    that.friendList = [{"username": "好友系统暂未完善", group: ""}]
                    that.$notify({
                        type: 'info',
                        message: res.data.msg
                    });
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: err.data.msg
                    });
                });
        },

        recoveryUserFileFromBin(index) {
            let that = this
            axios.get(diskAPI + "/recoveryUserFileFromBin?" + "fileDir=" + this.fileDir + "&fileName=" + this.binList[index].fileName)
                .then(function (res) {
                    if (res.data.code === RecoveryUserFileFromBin_Success.code) {
                        that.$notify({
                            type: 'success',
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
            setTimeout(() => {
                this.lookupAllFilesFromBin();
            }, 1000)
        },

        clearUserFileFromBin(index) {
            let that = this
            axios.get(diskAPI + "/clearUserFileFromBin?" + "fileDir=" + this.fileDir + "&fileName=" + this.binList[index].fileName)
                .then(function (res) {
                    if (res.data.code === ClearUserFileFromBin_Success.code) {
                        that.$notify({
                            type: 'success',
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
            setTimeout(() => {
                this.lookupAllFilesFromBin();
            }, 1000)
        },

        clearAllFilesFromBin() {
            let that = this
            axios.get(diskAPI + "/clearAllFilesFromBin")
                .then(function (res) {
                    if (res.data.code === ClearAllFilesFromBin_Success.code) {
                        that.$notify({
                            type: 'success',
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
            setTimeout(() => {
                this.lookupAllFilesFromBin();
            }, 1000)
        },

        Delete(index) {
            let data = "fileDir=" + this.fileDir + "&fileName=" + this.fileList[index].fileName;
            this.deleteUserFile(data);
            setTimeout(() => {
                this.getUserFileList();
            }, 1000)
        },

        DeleteSelect() {
            let len = this.selection.length;
            for (let i = 0; i < len; i++) {
                let data = "fileDir=" + this.fileDir + "&fileName=" + this.selection[i].fileName;
                this.deleteUserFile(data);
            }
            setTimeout(() => {
                this.getUserFileList();
            }, 2000)
        },

        deleteUserFile(data) {
            let that = this;
            axios.get(diskAPI + "/deleteUserFile?" + data)
                .then(function (res) {
                    if (res.data.code === DeleteUserFile_Success.code) {
                        that.$notify({
                            type: 'success',
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

        downloadFile(index) {
            this.$confirm('确认下载？', '是否下载?', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let data = "fileDir=" + this.fileDir + "&fileName=" + this.fileList[index].fileName;
                this.download(data, this.fileList[index].fileName);
            }).catch(() => {
                this.$notify({
                    type: 'info',
                    message: '已取消'
                });
            });
        },

        downloadSelect() {
            this.$confirm('确认下载？', '是否下载?', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let len = this.selection.length;
                for (let i = 0; i < len; i++) {
                    let data = "fileDir=" + this.fileDir + "&fileName=" + this.selection[i].fileName;
                    this.download(data, this.selection[i].fileName);
                }
            }).catch(() => {
                this.$notify({
                    type: 'info',
                    message: '已取消'
                });
            });
        },

        download(data, name) {
            let that = this
            that.$notify({
                type: 'success',
                message: name + ' 开始下载'
            });
            axios.post(diskAPI + "/downloadFile", data, {responseType: 'blob'})
                .then(function (res) {
                    const blob = new Blob([res.data])
                    const blobUrl = window.URL.createObjectURL(blob)
                    const a = document.createElement('a')
                    a.download = name
                    a.href = blobUrl
                    a.click()
                    setTimeout(() => {
                        document.body.removeChild(a);
                    }, 1000)
                    that.$notify({
                        type: 'success',
                        message: name + ' 下载成功'
                    });
                })
                .catch(function (err) {
                    that.$notify({
                        type: 'error',
                        message: err.message
                    });
                });
        },

    }
})
