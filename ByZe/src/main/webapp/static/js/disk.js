
new Vue({
    el: "#disk",
    created() {
        this.initData()
    },
    data() {
        return {
            userData: {
                username: "",
                grade: "",
            },
            fileList: [],
            checked: false,
            search: '',
            // 自己的数据
            url: basePath + '/byzeDisk',
            business: {
                initData: "201",
            },
        }
    },
    methods: {
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePreview(file) {
            console.log(file);
        },
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeRemove(file, fileList) {
            return this.$confirm(`确定移除 ${file.name}？`);
        },


        //  自己的函数
        initData: function () {
            let data = "business=" + this.business.initData;
            let that = this
            // 获得用户数据
            axios.post(this.url, data)
                .then(function (res) {
                    const str = res.data;
                    console.log(str)
                    let item = that.getData(str);
                    that.userData.username = item.username;
                    that.userData.grade = item.grade;
                    that.fileList = item.fileList;
                })
                .catch(function (err) {
                    that.userData.username = '未登录';
                    that.userData.grade = '0';
                    console.log("请求失败");
                });
        },

        Edit(index, row) {
            console.log(index, row);
        },
        Download(index, row) {
            console.log(index, row);
        },
        Delete(index, row) {
            console.log(index, row);
        },

        // 自己用的函数
        getData(str) {
            let file = {
                fileName: '',
                fileSize: '',
                createTime: ''
            }
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
                        file.fileName = paramList[i].split('=')[1].split(',')[0]
                        file.fileSize = paramList[i].split('=')[1].split(',')[1]
                        file.createTime = paramList[i].split('=')[1].split(',')[2]
                        item.fileList.push(file)
                        break;
                }
            }
            return item;
        }
    }
})

