package live.baize.server.controller;

import live.baize.server.bean.business.User;
import live.baize.server.bean.business.disk.DiskData;
import live.baize.server.bean.business.disk.UserFile;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.disk.DiskUtil;
import live.baize.server.service.user.SessionUtil;
import live.baize.server.service.user.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/baizeDisk")
public class DiskController {
    @Resource
    private UserUtil userUtil;
    @Resource
    private DiskUtil diskUtil;
    @Resource
    private SessionUtil sessionUtil;

    /**
     * 返回用户数据和网盘数据
     */
    @GetMapping("/getUserDiskData")
    public Response getUserDiskData() {
        User user = userUtil.getUserBasicInfoByEmail(sessionUtil.getUserFromSession().getEmail());
        DiskData diskData = diskUtil.getDiskDataByUId(user.getUId());
        Map<String, Object> data = new HashMap<>();
        data.put("userData", user);
        data.put("diskData", diskData);
        return new Response(ResponseEnum.GetUserDiskData_Success, data);
    }

    /**
     * 返回好友列表
     */
    @GetMapping("/getFriendList")
    public Response getFriendList() {
        return new Response(ResponseEnum.GetFriendList_Failure, null);
    }

    /**
     * 返回用户文件列表
     */
    @GetMapping("/getUserFileList")
    public Response getUserFileList(@RequestParam String fileDir) {
        // TODO：考虑分页
        Integer UId = sessionUtil.getUserFromSession().getUId();
        List<UserFile> fileList = diskUtil.getUserFileAtDir(UId, fileDir);
        return new Response(ResponseEnum.GetUserFileList_Success, fileList);
    }

    /**
     * 接收上传的文件
     * 前端必须传一个fileDir
     */
    @PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam("fileDir") String fileDir, @RequestParam("file") MultipartFile file) {
        // 判断 fileName 是否为空
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return new Response(ResponseEnum.FileNameIsNull, null);
        }

        Integer UId = sessionUtil.getUserFromSession().getUId();
        if (!diskUtil.saveUserFile(UId, fileDir, fileName, file)) {
            return new Response(ResponseEnum.SYSTEM_UNKNOWN, null);
        }
        return new Response(ResponseEnum.UploadFile_Success, null);
    }

    /**
     * 下载文件
     */
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestParam("fileDir") String fileDir, @RequestParam("fileName") String fileName) {
        Integer UId = sessionUtil.getUserFromSession().getUId();
        if (!diskUtil.downloadFile(UId, fileDir, fileName, sessionUtil.getResponse())) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN, null);
        }
    }

    /**
     * 删除指定文件
     */
    @GetMapping("/deleteUserFile")
    public Response deleteUserFile(@RequestParam("fileDir") String fileDir, @RequestParam("fileName") String fileName) {
        Integer UId = sessionUtil.getUserFromSession().getUId();
        if (!diskUtil.deleteUserFile(UId, fileName, fileDir)) {
            return new Response(ResponseEnum.SYSTEM_UNKNOWN, null);
        }
        return new Response(ResponseEnum.DeleteUserFile_Success, null);
    }

    /**
     * 还原垃圾箱中的某个文件
     */
    @GetMapping("/recoveryUserFileFromBin")
    public Response recoveryFileFromBin(@RequestParam("fileDir") String fileDir, @RequestParam("fileName") String fileName) {
        Integer UId = sessionUtil.getUserFromSession().getUId();
        if (!diskUtil.recoveryUserFileFromBin(UId, fileName, fileDir)) {
            return new Response(ResponseEnum.RecoveryUserFileFromBin_Failure, null);
        }
        return new Response(ResponseEnum.RecoveryUserFileFromBin_Success, null);
    }

    /**
     * 清理垃圾箱中的某个文件
     */
    @GetMapping("/clearUserFileFromBin")
    public Response clearUserFileFromBin(@RequestParam("fileDir") String fileDir, @RequestParam("fileName") String fileName) {
        Integer UId = sessionUtil.getUserFromSession().getUId();
        if (!diskUtil.clearUserFileFromBin(UId, fileName, fileDir)) {
            return new Response(ResponseEnum.ClearUserFileFromBin_Failure, null);
        }
        return new Response(ResponseEnum.ClearUserFileFromBin_Success, null);
    }

    /**
     * 清空垃圾箱
     */
    @GetMapping("/clearAllFilesFromBin")
    public Response clearAllFilesFromBin() {
        Integer UId = sessionUtil.getUserFromSession().getUId();
        if (!diskUtil.clearAllFilesFromBin(UId)) {
            return new Response(ResponseEnum.SYSTEM_UNKNOWN, null);
        }
        return new Response(ResponseEnum.ClearAllFilesFromBin_Success, null);
    }

    /**
     * 查看垃圾箱
     */
    @GetMapping("/lookupAllFilesFromBin")
    public Response lookupAllFilesFromBin() {
        Integer UId = sessionUtil.getUserFromSession().getUId();
        List<UserFile> fileList = diskUtil.lookupAllFilesFromBin(UId);
        return new Response(ResponseEnum.LookupAllFilesFromBin_Success, fileList);
    }

}
