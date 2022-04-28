new Vue({
    el: "#pan",
    created() {
        this.updateData()
    },
    data() {
        const item = {
            fileName: 'file.txt',
            fileSize: '100MB',
            createTime: '2016-05-02'
        };
        return {
            userData: {
                username:"",
                userRank:"",
            },
            fileData: [item, item],
            fileList: [],
            checked: false,
            search: '',
            // 自己的数据
            url: 'http://localhost:8080/ByZe/byzepan',
            business: {
                hello: "201",
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
        updateUserData: function () {
            // 更新用户数据
            this.userData.username = "小白";
            this.userData.userRank = 0;
        },
        updateFileData: function () {
            // 更新文件数据

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
