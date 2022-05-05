//
// function getData(str) {
//     let item = {
//         username: '',
//         grade: '',
//         fileList: [],
//     }
//     const paramList = str.split('&')
//     for (let i = 0; i < paramList.length; ++i) {
//         switch (paramList[i].split('=')[0]) {
//             case "username":
//                 item.username = paramList[i].split('=')[1]
//                 break;
//             case "grade":
//                 item.grade = paramList[i].split('=')[1]
//                 break;
//             case "file":
//                 let file = {
//                     fileName: '',
//                     fileSize: '',
//                     createTime: '2022-5-5'
//                 }
//                 file.fileName = paramList[i].split('=')[1].split(',')[0]
//                 file.fileSize = paramList[i].split('=')[1].split(',')[1].split('|')[0] + "M"
//                 item.fileList.push(file)
//                 break;
//         }
//     }
//     return item;
// }
//
// str = 'username=MUTOU&uid=10000000&grade=1&file=国徽.jpg,2|&file=壁纸2.png,2|'
// item = getData(str)
// console.log(item)
//
// // //==================================
// // // 修改时间+文件名称+最后修改时间-->MD5
// // function md5File(file) {
// //     return new Promise((resolve, reject) => {
// //         let blobSlice =
// //             File.prototype.slice ||
// //             File.prototype.mozSlice ||
// //             File.prototype.webkitSlice
// //         let chunkSize = file.size / 100
// //         let chunks = 100
// //         let currentChunk = 0
// //         let spark = new SparkMD5.ArrayBuffer()
// //         let fileReader = new FileReader()
// //         fileReader.onload = function (e) {
// //             console.log('read chunk nr', currentChunk + 1, 'of', chunks)
// //             spark.append(e.target.result) // Append array buffer
// //             currentChunk++
// //             if (currentChunk < chunks) {
// //                 loadNext()
// //             } else {
// //                 let cur = +new Date()
// //                 console.log('finished loading')
// //                 // alert(spark.end() + '---' + (cur - pre)); // Compute hash
// //                 let result = spark.end()
// //                 resolve(result)
// //             }
// //         }
// //         fileReader.onerror = function (err) {
// //             console.warn('oops, something went wrong.')
// //             reject(err)
// //         }
// //
// //         function loadNext() {
// //             let start = currentChunk * chunkSize
// //             let end =
// //                 start + chunkSize >= file.size ? file.size : start + chunkSize
// //             fileReader.readAsArrayBuffer(blobSlice.call(file, start, end))
// //         }
// //
// //         loadNext()
// //     })
// // }
// //
// // // 校验文件的MD5
// // function checkFileMD5(file, fileName, fileMd5Value, onError) {
// //     const fileSize = file.size
// //     const {chunkSize, uploadProgress} = this
// //     this.chunks = Math.ceil(fileSize / chunkSize)
// //     return new Promise(async (resolve, reject) => {
// //         const params = {
// //             fileName: fileName,
// //             fileMd5Value: fileMd5Value,
// //         }
// //         const {ok, data} = await services.checkFile(params)
// //         if (ok) {
// //             this.hasUploaded = data.chunkList.length
// //             uploadProgress(file)
// //             resolve(data)
// //         } else {
// //             reject(ok)
// //             onError()
// //         }
// //     })
// // }
// //
// // async function checkAndUploadChunk(file, fileMd5Value, chunkList) {
// //     let {chunks, upload} = this
// //     const requestList = []
// //     for (let i = 0; i < chunks; i++) {
// //         let exit = chunkList.indexOf(i + '') > -1
// //         // 如果已经存在, 则不用再上传当前块
// //         if (!exit) {
// //             requestList.push(upload(i, fileMd5Value, file))
// //         }
// //     }
// //     console.log({requestList})
// //     const result =
// //         requestList.length > 0
// //             ? await Promise.all(requestList)
// //                 .then(result => {
// //                     console.log({result})
// //                     return result.every(i => i.ok)
// //                 })
// //                 .catch(err => {
// //                     return err
// //                 })
// //             : true
// //     console.log({result})
// //     return result === true
// // }
// //
// // // 上传chunk
// // function upload(i, fileMd5Value, file) {
// //     const {uploadProgress, chunks} = this
// //     return new Promise((resolve, reject) => {
// //         let {chunkSize} = this
// //         // 构造一个表单，FormData是HTML5新增的
// //         let end =
// //             (i + 1) * chunkSize >= file.size ? file.size : (i + 1) * chunkSize
// //         let form = new FormData()
// //         form.append('data', file.slice(i * chunkSize, end)) // file对象的slice方法用于切出文件的一部分
// //         form.append('total', chunks) // 总片数
// //         form.append('index', i) // 当前是第几片
// //         form.append('fileMd5Value', fileMd5Value)
// //         services
// //             .uploadLarge(form)
// //             .then(data => {
// //                 if (data.ok) {
// //                     this.hasUploaded++
// //                     uploadProgress(file)
// //                 }
// //                 console.log({data})
// //                 resolve(data)
// //             })
// //             .catch(err => {
// //                 reject(err)
// //             })
// //     })
// // }
// //
// // const config = {
// //     onUploadProgress: progressEvent => {
// //         let complete = (progressEvent.loaded / progressEvent.total * 100 | 0) + '%'
// //     }
// // }
// // services.uploadChunk(form, config)
// //
// //
// // // 合并文件
// // exports.merge = {
// //     validate: {
// //         query: {
// //             fileName: Joi.string()
// //                 .trim()
// //                 .required()
// //                 .description('文件名称'),
// //             md5: Joi.string()
// //                 .trim()
// //                 .required()
// //                 .description('文件md5'),
// //             size: Joi.string()
// //                 .trim()
// //                 .required()
// //                 .description('文件大小'),
// //         },
// //     },
// //     permission: {
// //         roles: ['user'],
// //     },
// //     async handler(ctx) {
// //         const {fileName, md5, size} = ctx.request.query
// //         let {name, base: filename, ext} = path.parse(fileName)
// //         const newFileName = randomFilename(name, ext)
// //         await mergeFiles(path.join(uploadDir, md5), uploadDir, newFileName, size)
// //             .then(async () => {
// //                 const file = {
// //                     key: newFileName,
// //                     name: filename,
// //                     mime_type: mime.getType(`${uploadDir}/${newFileName}`),
// //                     ext,
// //                     path: `${uploadDir}/${newFileName}`,
// //                     provider: 'oss',
// //                     size,
// //                     owner: ctx.state.user.id,
// //                 }
// //                 const key = encodeURIComponent(file.key)
// //                     .replace(/%/g, '')
// //                     .slice(-100)
// //                 file.url = await uploadLocalFileToOss(file.path, key)
// //                 file.url = getFileUrl(file)
// //                 const f = await File.create(omit(file, 'path'))
// //                 const files = []
// //                 files.push(f)
// //                 ctx.body = invokeMap(files, 'toJSON')
// //             })
// //             .catch(() => {
// //                 throw Boom.badData('大文件分片合并失败，请稍候重试~')
// //             })
// //     },
// // }