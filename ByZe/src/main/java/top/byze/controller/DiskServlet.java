package top.byze.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import top.byze.bean.PanData;
import top.byze.bean.User;
import top.byze.bean.UserFile;
import top.byze.service.Disk;
import top.byze.utils.ConfigUtil;
import top.byze.utils.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author CodeXS
 */
@Slf4j
@WebServlet(value = "/disk")
public class DiskServlet extends HttpServlet {
    private void doException() {
        log.error("前端数据异常");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        String business = req.getParameter("business");
        log.info("业务: " + business);
        if (business == null) {
            doException();
            return;
        }

        switch (business) {
            case Business.INIT_DATA:
                initData(req, res);
                break;
            case Business.DOWNLOAD_FILE:
                downloadFile(req, res);
                break;
            case Business.UPDATE_FILE_LIST:
                updateFileList(req, res);
                break;
            case Business.DELETE_FILE:
                deleteFile(req, res);
                break;
            case Business.LOOKUP_BIN:
                lookupBin(req, res);
                break;
            case Business.CLEAR_BIN:
                clearBin(req, res);
                break;
            case Business.CLEAR_USER_FILE:
                clearUserFile(req, res);
                break;
            case Business.RECOVERY_FILE:
                recoveryFile(req, res);
                break;
            case Business.ATTRIBUTES:
                attributes(req, res);
                break;
            case Business.GET_FRIEND:
                getFriend(req, res);
                break;
            case Business.MODIFY_ATTRIBUTES:
                modifyAttributes(req, res);
                break;
            default:
                doException();
                break;
        }
    }

    /**
     * 初始化界面
     */
    private void initData(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        // 获得网盘数据
        PanData panData = Disk.getPanData(user.getUid());
        // 获得文件数据
        List<UserFile> fileList = Disk.getFileList(user.getUid(), "/");
        // 返回数据
        StringBuilder stringBuilder = new StringBuilder();
        // 添加用户属性
        Disk.addAttributes(stringBuilder, user, panData);
        // 添加文件列表
        Disk.addFileList(stringBuilder, fileList);
        // 告诉前端
        try {
            res.getWriter().println(stringBuilder);
            log.info(user.getEmail() + " 初始化数据正常");
        } catch (IOException e) {
            log.error(user.getEmail() + " 初始化数据异常");
        }
    }

    /**
     * 下载文件
     */
    private void downloadFile(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        String fileDir = req.getParameter("fileDir");
        String fileName = req.getParameter("fileName");
        // 得到要下载的文件
        File file = new File(ConfigUtil.getUserFilePath() + "User" + user.getUid() + fileDir + fileName);
        try {
            //如果文件不存在，则显示下载失败
            if (!file.exists()) {
                try {
                    res.getWriter().println(Res.FALSE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info(user.getEmail() + " " + fileName + " 下载失败");
            } else {
                // 设置相应头，控制浏览器下载该文件，这里就是会出现当你点击下载后，出现的下载地址框
                res.setContentType("application/octet-stream");
                res.addHeader("Content-Disposition", "attachment;filename=" + fileName + ";filename*=UTF-8");
                OutputStream os = res.getOutputStream();
                FileUtils.copyFile(file, os);
                os.flush();
                os.close();
                log.info(user.getEmail() + " " + fileName + " 下载成功");
            }
        } catch (Exception e) {
            log.info("出现异常");
        }
    }

    /**
     * 更新页面
     */
    private void updateFileList(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        // 获得文件目录
        String fileDir = req.getParameter("fileDir");
        // 获得文件数据
        List<UserFile> fileList = Disk.getFileList(user.getUid(), fileDir);
        // 返回数据
        StringBuilder stringBuilder = new StringBuilder();
        Disk.addFileList(stringBuilder, fileList);
        // 告诉前端
        try {
            res.getWriter().println(stringBuilder);
            log.info(user.getEmail() + " 页面更新成功");
        } catch (IOException e) {
            log.info(user.getEmail() + " 页面更新失败");
        }
    }

    /**
     * 删除指定文件
     */
    private void deleteFile(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        int uid = user.getUid();
        String fileDir = req.getParameter("fileDir");
        String fileName = req.getParameter("fileName");
        // 设置UserFile中文件状态为N 表示删除 所以此时不释放空间
        Disk.deleteUserFile(uid, fileName, fileDir);
        // 告诉前端
        try {
            res.getWriter().println(Res.TRUE);
            log.info(user.getEmail() + " 删除文件成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 删除文件失败");
        }
    }

    /**
     * 查看垃圾箱
     */
    private void lookupBin(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        Integer uid = user.getUid();
        // 查看垃圾箱前 删除过期文件
        PanData panData = Disk.getPanData(uid);
        Integer days = panData.getOutOfDate();
        List<UserFile> fileList = Disk.selectFilesOutOfDateInDatabase(uid, days);
        // 删除磁盘中的过期文件
        Disk.clearFileListInDir(uid, fileList);
        // 删除数据库中的过期文件
        Disk.clearFilesOutOfDateInDatabase(uid, days);
        // 查看垃圾箱
        fileList = Disk.lookupBin(uid);
        StringBuilder stringBuilder = new StringBuilder();
        Disk.addFileList(stringBuilder, fileList);
        // 告诉前端
        try {
            res.getWriter().println(stringBuilder);
            log.info(user.getEmail() + " 查看回收站成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 查看回收站失败");
        }
    }

    /**
     * 清理垃圾箱中的某个文件
     */
    private void clearUserFile(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        int uid = user.getUid();
        String fileDir = req.getParameter("fileDir");
        String fileName = req.getParameter("fileName");
        // 添加Size
        PanData panData = Disk.getPanData(uid);
        List<UserFile> fileList = Disk.getUserFile(uid, fileName, fileDir);
        Integer fileSizeSum = Disk.getFileSizeSum(fileList);
        // 通过所保证数据的正确性
        synchronized (req.getSession()) {
            Integer nowStorage = panData.getNowStorage() - fileSizeSum;
            Disk.setNowStorage(uid, nowStorage);
        }
        // 清空文件
        Disk.clearUserFile(uid, fileName, fileDir);
        // 在磁盘上删除
        Disk.clearFileListInDir(uid, fileList);
        // 告诉前端
        try {
            res.getWriter().println(Res.TRUE);
            log.info(user.getEmail() + " 清除文件成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 清除文件失败");
        }
    }

    /**
     * 清空垃圾箱
     */
    private void clearBin(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        int uid = user.getUid();
        // 添加Size
        PanData panData = Disk.getPanData(uid);
        List<UserFile> fileList = Disk.lookupBin(uid);
        Integer fileSizeSum = Disk.getFileSizeSum(fileList);
        Integer nowStorage = panData.getNowStorage() - fileSizeSum;
        Disk.setNowStorage(uid, nowStorage);
        // 清空回收站 数据库
        Disk.clearBin(uid);
        // 在磁盘上删除
        Disk.clearFileListInDir(uid, fileList);
        // 告诉前端
        try {
            res.getWriter().println(Res.TRUE);
            log.info(user.getEmail() + " 清空回收站成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 清空回收站失败");
        }
    }

    /**
     * 还原垃圾箱中的某个文件
     */
    private void recoveryFile(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        int uid = user.getUid();
        String fileDir = req.getParameter("fileDir");
        String fileName = req.getParameter("fileName");
        // 恢复文件
        Disk.recoveryFile(uid, fileName, fileDir);
        // 告诉前端
        try {
            res.getWriter().println(Res.TRUE);
            log.info(user.getEmail() + " 还原文件成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 还原文件失败");
        }
    }

    /**
     * 返回用户属性
     */
    private void attributes(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = Disk.getUser(user.getEmail());
        // 获得网盘数据
        PanData panData = Disk.getPanData(user.getUid());
        StringBuilder stringBuilder = new StringBuilder();
        // 添加用户属性
        Disk.addAttributes(stringBuilder, user, panData);
        // 告诉前端
        try {
            res.getWriter().println(stringBuilder);
            log.info(user.getEmail() + " 返回用户属性成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 返回用户属性失败");
        }
    }

    /**
     * 返回好友列表
     */
    private void getFriend(HttpServletRequest req, HttpServletResponse res) {
        // 告诉前端
        try {
            res.getWriter().println("好友系统, 没有完善");
            log.info("返回好友列表成功");
        } catch (Exception e) {
            log.info("返回好友列表失败");
        }
    }

    /**
     * 修改用户属性
     */
    private void modifyAttributes(HttpServletRequest req, HttpServletResponse res) {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得要修改的参数
        String idCard = req.getParameter("idCard");
        String realName = req.getParameter("realName");
        String phone = req.getParameter("phone");
        Disk.modifyAttributes(user.getEmail(), idCard, realName, phone);
        // 告诉前端
        try {
            res.getWriter().println(Res.TRUE);
            log.info(user.getEmail() + " 修改用户属性成功");
        } catch (Exception e) {
            log.info(user.getEmail() + " 修改用户属性失败");
        }
    }

    /**
     * 业务类型
     */
    private static class Business {
        final static String INIT_DATA = "201";
        final static String DOWNLOAD_FILE = "202";
        final static String UPDATE_FILE_LIST = "203";
        final static String DELETE_FILE = "204";

        final static String LOOKUP_BIN = "205";
        final static String CLEAR_BIN = "206";
        final static String CLEAR_USER_FILE = "207";
        final static String RECOVERY_FILE = "208";
        final static String ATTRIBUTES = "209";
        final static String GET_FRIEND = "210";
        final static String MODIFY_ATTRIBUTES = "211";
    }

    /**
     * 返回值
     */
    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }
}
