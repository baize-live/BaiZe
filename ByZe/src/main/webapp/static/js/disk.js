new Vue({
    el: "#disk",
    created() {
        this.initData()
    },
    data() {
        return {
            userData: {
                username: "",
                userRank: "",
            },
            fileData: [],
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
            axios.post(this.url, data)
                .then(function (res) {
                    console.log(res.data);
                })
                .catch(function (err) {
                    console.log("请求失败");
                });
            // TODO: 写到这里了
            // 更新用户数据
            this.userData.username = "小白";
            this.userData.userRank = 0;
            const item = {
                fileName: '',
                fileSize: '',
                createTime: ''
            };
            this.fileData.push(item)
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
    }
})
