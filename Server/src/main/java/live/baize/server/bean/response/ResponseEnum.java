package live.baize.server.bean.response;

import lombok.Getter;


@Getter
public class ResponseEnum {

    // ============================================= UserController ============================================= //
    public static final ResponseEnum Has_Email = new ResponseEnum(200010, "邮箱已被注册");
    public static final ResponseEnum Not_Email = new ResponseEnum(200011, "邮箱未被注册");
    public static final ResponseEnum VerifyCode_Send_Failure = new ResponseEnum(200020, "验证码发送失败");
    public static final ResponseEnum VerifyCode_Send_Success = new ResponseEnum(200021, "验证码发送成功");
    public static final ResponseEnum Register_Failure = new ResponseEnum(200030, "注册失败, 验证码错误");
    public static final ResponseEnum Register_Success = new ResponseEnum(200031, "注册成功");
    public static final ResponseEnum Login_Failure = new ResponseEnum(200040, "登录失败, 邮箱或密码错误");
    public static final ResponseEnum Login_Success = new ResponseEnum(200041, "登录成功");
    public static final ResponseEnum Not_Login = new ResponseEnum(200050, "没有登录");
    public static final ResponseEnum Has_Login = new ResponseEnum(200051, "已经登录");
    public static final ResponseEnum Logout_Success = new ResponseEnum(200061, "退出登录成功");
    public static final ResponseEnum OpenDisk_Failure = new ResponseEnum(200070, "开通白泽网盘失败");
    public static final ResponseEnum OpenDisk_Success = new ResponseEnum(200071, "开通白泽网盘成功");
    public static final ResponseEnum Not_OpenDisk = new ResponseEnum(200080, "白泽网盘暂未开通");
    public static final ResponseEnum Has_OpenDisk = new ResponseEnum(200081, "白泽网盘已经开通");
    public static final ResponseEnum OpenGame_Failure = new ResponseEnum(200090, "开通白泽游戏失败");
    public static final ResponseEnum OpenGame_Success = new ResponseEnum(200091, "开通白泽游戏成功");
    public static final ResponseEnum Not_OpenGame = new ResponseEnum(200100, "白泽游戏暂未开通");
    public static final ResponseEnum Has_OpenGame = new ResponseEnum(200101, "白泽游戏已经开通");
    public static final ResponseEnum ModifyUserData_Success = new ResponseEnum(200111, "修改用户属性成功");

    // ============================================= DiskController ============================================= //
    public static final ResponseEnum GetUserFileList_Success = new ResponseEnum(600001, "用户文件列表获取成功");
    public static final ResponseEnum GetUserDiskData_Success = new ResponseEnum(600011, "用户网盘数据获取成功");
    public static final ResponseEnum CreateFileDir_Failure = new ResponseEnum(600020, "创建文件夹失败");
    public static final ResponseEnum FileNameIsNull = new ResponseEnum(600030, "文件名为null");
    public static final ResponseEnum WriteFile_Failure = new ResponseEnum(600040, "保存文件失败");
    public static final ResponseEnum UploadFile_Success = new ResponseEnum(600051, "上传文件成功");
    public static final ResponseEnum StorageNotEnough = new ResponseEnum(600060, "用户存储空间不足");
    public static final ResponseEnum MaxUploadSizeExceeded = new ResponseEnum(600070, "网页端无法上传大文件");
    public static final ResponseEnum File_Has_Exist = new ResponseEnum(600080, "该文件已经存在");
    public static final ResponseEnum Not_ThisFile = new ResponseEnum(600090, "没有这个文件");
    public static final ResponseEnum GetFriendList_Failure = new ResponseEnum(600100, "好友业务尚未完善");
    public static final ResponseEnum DeleteUserFile_Success = new ResponseEnum(600111, "删除用户文件成功");
    public static final ResponseEnum RecoveryUserFileFromBin_Failure = new ResponseEnum(600120, "文件不存在或未删除");
    public static final ResponseEnum RecoveryUserFileFromBin_Success = new ResponseEnum(600121, "恢复用户文件成功");
    public static final ResponseEnum ClearUserFileFromBin_Failure = new ResponseEnum(600130, "文件不存在或未删除");
    public static final ResponseEnum ClearUserFileFromBin_Success = new ResponseEnum(600131, "清理用户文件成功");
    public static final ResponseEnum LookupAllFilesFromBin_Success = new ResponseEnum(600141, "查看回收站成功");
    public static final ResponseEnum ClearAllFilesFromBin_Success = new ResponseEnum(600151, "清空回收站成功");

    // ============================================= 通用 ============================================= //
    public static final ResponseEnum Hello_World = new ResponseEnum(100000, "你好, 世界! 你好, 朋友!");

    public static final ResponseEnum Param_Check = new ResponseEnum(400000, "参数有误, 请检查");
    public static final ResponseEnum Param_Missing = new ResponseEnum(400010, "参数缺少, 请检查");
    public static final ResponseEnum MethodNotSupported = new ResponseEnum(400020, "请求方法不支持");
    public static final ResponseEnum Not_IsMultipart = new ResponseEnum(400030, "不是一个Multipart 请求");
    public static final ResponseEnum Request_Part_Missing = new ResponseEnum(400040, "请求体参数缺少, 请检查");

    public static final ResponseEnum SYSTEM_UNKNOWN = new ResponseEnum(500000, "系统繁忙");

    final String msg;
    final Integer code;

    ResponseEnum(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
