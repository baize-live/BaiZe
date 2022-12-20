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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/disk")
public class DiskController {
    @Resource
    private UserUtil userUtil;
    @Resource
    private DiskUtil diskUtil;
    @Resource
    private SessionUtil sessionUtil;

    @GetMapping("/getUserDiskData")
    public Response getUserDiskData() {
        User user = userUtil.getUserBasicInfoByEmail(sessionUtil.getUserFromSession().getEmail());
        DiskData diskData = diskUtil.getDiskDataByUId(user.getUId());
        Object[] data = {user, diskData};
        return new Response(ResponseEnum.GetUserDiskData_Success, data);
    }

    @GetMapping("/getUserFileList")
    public Response getUserFileList(@RequestParam String fileDir) {
        Integer UId = userUtil.getUserIdByEmail(sessionUtil.getUserFromSession().getEmail());
        List<UserFile> fileList = diskUtil.getUserFileAtDir(UId, fileDir);
        return new Response(ResponseEnum.GetUserFileList_Success, fileList);
    }

    /**
     * 接收上传的文件
     * 前端必须传一个fileDir
     */
    @PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam("fileDir") String fileDir,
                               @RequestParam("file") MultipartFile file) {
        // 判断 fileName 是否为空
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return new Response(ResponseEnum.FileNameIsNull, null);
        }

        // 获得UID
        Integer UId = userUtil.getUserIdByEmail(sessionUtil.getUserFromSession().getEmail());

        // 保存文件
        if (!diskUtil.saveUserFile(UId, fileDir, fileName, file)) {
            return new Response(ResponseEnum.SYSTEM_UNKNOWN, null);
        }
        return new Response(ResponseEnum.UploadFile_Success, null);
    }


    /**
     * 下载文件
     */
    @PostMapping("/downloadFile")
    private void downloadFile(@RequestParam("fileDir") String fileDir,
                              @RequestParam("fileName") String fileName,
                              HttpServletResponse response) {
        Integer UId = userUtil.getUserIdByEmail(sessionUtil.getUserFromSession().getEmail());

        if (!diskUtil.downloadFile(UId, fileDir, fileName, response)) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN, null);
        }
    }
    
}
