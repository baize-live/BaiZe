package top.byze.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import top.byze.bean.FromMap;
import top.byze.bean.PanData;
import top.byze.bean.User;
import top.byze.service.Disk;
import top.byze.utils.ConfigUtil;
import top.byze.utils.FileUtil;
import top.byze.utils.FromUtil;
import top.byze.utils.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author CodeXS
 */
@Slf4j
@WebServlet(value = "/uploadFile")
public class UploadFileServlet extends HttpServlet {

    private void doWork(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        PanData panData = Disk.getPanData(user.getUid());
        String path = ConfigUtil.getUserFilePath() + "User" + user.getUid();
        if (ServletFileUpload.isMultipartContent(req)) {
            FromMap fromMap = FromUtil.parseParam(req);
            // 前端传来的currentDir
            String fileDir = fromMap.getParamMap().get("currentDir");
            Map<String, FileItem> map = fromMap.getFileMap();
            for (String name : map.keySet()) {
                Integer fileSize = (int) (Math.round(map.get(name).getSize() * 1.0 / 1024 / 1024));
                // 保存在服务器上
                FileUtil.saveFile(map.get(name), path + fileDir + name);
                // 存储在数据库中
                Disk.saveUserFile(user.getUid(), name, "-", fileSize, fileDir);
                // 增大当前存储
                synchronized (req.getSession()) {
                    Integer nowStorage = panData.getNowStorage() + fileSize;
                    Disk.setNowStorage(user.getUid(), nowStorage);
                }
                log.info(user.getEmail() + " 文件 " + name + " 保存成功");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

}
