let basePath = 'http://www.baize.live/baizeServer'
let userAPI = basePath + "/user"
let userAPI_hasLogin = userAPI + "/hasLogin"
let diskAPI = basePath + "/baizeDisk"

class response {
    code;
    msg;

    constructor(code, msg) {
        this.msg = msg
        this.code = code
    }
}
// userAPI
Has_Email = new response(200010, "邮箱已被注册")
Not_Email = new response(200011, "邮箱未被注册")
VerifyCode_Send_Failure = new response(200020, "验证码发送失败")
VerifyCode_Send_Success = new response(200021, "验证码发送成功")
Register_Failure = new response(200030, "注册失败, 验证码错误")
Register_Success = new response(200031, "注册成功")
Login_Failure = new response(200040, "登录失败, 邮箱或密码错误")
Login_Success = new response(200041, "登录成功")
Not_Login = new response(200050, "没有登录")
Has_Login = new response(200051, "已经登录")
Logout_Success = new response(200061, "退出登录成功")
OpenDisk_Failure = new response(200070, "开通白泽网盘失败")
OpenDisk_Success = new response(200071, "开通白泽网盘成功")
Not_OpenDisk = new response(200080, "白泽网盘暂未开通")
Has_OpenDisk = new response(200081, "白泽网盘已经开通")
OpenGame_Failure = new response(200090, "开通白泽游戏失败")
OpenGame_Success = new response(200091, "开通白泽游戏成功")
Not_OpenGame = new response(200100, "白泽游戏暂未开通")
Has_OpenGame = new response(200101, "白泽游戏已经开通")
ModifyUserData_Success = new response(200111, "修改用户属性成功")

// diskAPI
GetUserFileList_Success = new response(600001, "用户文件列表获取成功");
GetUserDiskData_Success = new response(600011, "用户网盘数据获取成功");
CreateFileDir_Failure = new response(600020, "创建文件夹失败");
FileNameIsNull = new response(600030, "文件名为null");
WriteFile_Failure = new response(600040, "保存文件失败");
UploadFile_Success = new response(600051, "上传文件成功");
StorageNotEnough = new response(600060, "用户存储空间不足");
MaxUploadSizeExceeded = new response(600070, "网页端无法上传大文件");
File_Has_Exist = new response(600080, "该文件已经存在");
Not_ThisFile = new response(600090, "没有这个文件");
GetFriendList_Failure = new response(600100, "好友业务尚未完善");
DeleteUserFile_Success = new response(600111, "删除用户文件成功");
RecoveryUserFileFromBin_Failure = new response(600120, "文件不存在或未删除");
RecoveryUserFileFromBin_Success = new response(600121, "恢复用户文件成功");
ClearUserFileFromBin_Failure = new response(600130, "文件不存在或未删除");
ClearUserFileFromBin_Success = new response(600131, "清理用户文件成功");
LookupAllFilesFromBin_Success = new response(600141, "查看回收站成功");
ClearAllFilesFromBin_Success = new response(600151, "清空回收站成功");

// 全局
Param_Check = new response(400000, "参数有误, 请检查");
Param_Missing = new response(400010, "参数缺少, 请检查");
MethodNotSupported = new response(400020, "请求方法不支持");
Not_IsMultipart = new response(400030, "不是一个Multipart 请求");
Request_Part_Missing = new response(400040, "请求体参数缺少, 请检查");
SYSTEM_UNKNOWN = new response(500000, "系统繁忙");


