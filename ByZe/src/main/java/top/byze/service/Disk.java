package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.FromMap;
import top.byze.bean.PanData;
import top.byze.bean.User;
import top.byze.bean.UserFile;
import top.byze.mapper.PanDataMapper;
import top.byze.mapper.UserFileMapper;
import top.byze.mapper.UserMapper;
import top.byze.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class Disk {
    HttpServletRequest req;
    HttpServletResponse res;

    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

    public Disk(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    // 与数据库交互
    // 获得用户属性
    private static User getUser(String email) {
        User user = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            user = userMapper.getUser(email);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("获得用户数据异常");
        }
        return user;
    }

    // 获得网盘数据
    private static PanData getPanData(int Uid) {
        PanData panData = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panData = panDataMapper.getPanData(Uid);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("获得网盘数据异常");
            e.printStackTrace();
        }
        return panData;
    }

    // 获得指定目录下的文件链表
    private static List<UserFile> getFileList(int uid, String fileDir) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.getFileList(uid, fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    // 查看指定文件
    private static List<UserFile> getUserFile(int uid, String fileName, String fileDir) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.getUserFile(uid, fileName, fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    // 查看回收站的所有文件
    private static List<UserFile> lookupBin(int uid) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.lookupBin(uid);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    // 查找数据库中的过期文件(表示回收站中过期的文件)
    private static List<UserFile> selectFilesOutOFfDateInDB(int uid, int days) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.selectFilesOutOFfDateInDB(uid, days);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    // 保存用户上传文件
    private static void saveUserFile(int uid, String fileName, char fileType, long fileSize, char fileState, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.saveUserFile(uid, fileName, fileType, fileSize, fileState, fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置当前存储
    private static void setNowStorage(int uid, long nowStorage) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panDataMapper.setNowStorage(uid, nowStorage);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除指定文件(修改标记而已, 即放到回收站)
    private static void deleteUserFile(int uid, String fileName, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.deleteUserFile(uid, fileName, fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清空回收站
    private static void clearBin(int uid) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.clearBin(uid);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清除指定文件
    private static void clearUserFile(int uid, String fileName, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.clearUserFile(uid, fileName, fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 恢复指定文件
    private static void recoveryFile(int uid, String fileName, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.recoveryFile(uid, fileName, fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清除数据库中的过期文件 这个时间可以个性化设置
    private static void clearFilesOutOFfDateInDB(int uid, int days) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.clearFilesOutOFfDateInDB(uid, days);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改用户属性
    private static void modifyAttributes(String email, String idCard, String realName, String phone) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.modifyAttributes(email, idCard, realName, phone);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =================功能函数
    // 生成用户属性字符串
    private static void addAttributes(StringBuilder stringBuilder, User user, PanData panData) {
        stringBuilder
                .append("username=").append(user.getUsername()).append("&")
                .append("uid=").append(user.getUid()).append("&")
                .append("email=").append(user.getEmail()).append("&")
                .append("idCard=").append(user.getIDCard() == null ? " " : user.getIDCard()).append("&")
                .append("realName=").append(user.getRealName() == null ? " " : user.getRealName()).append("&")
                .append("phone=").append(user.getPhone() == null ? " " : user.getPhone()).append("&")
                .append("icon=").append(panData.getIcon()).append("&")
                .append("nowStorage=").append(panData.getNowStorage()).append("&")
                .append("maxStorage=").append(panData.getMaxStorage()).append("&")
                .append("grade=").append(panData.getGrade()).append("&");
    }

    // 生成一个字符串(包含文件信息)
    private static void addFileList(StringBuilder stringBuilder, List<UserFile> fileList) {
        for (UserFile userFile : fileList) {
            stringBuilder
                    .append("file=")
                    .append(userFile.getFileName()).append(",")
                    .append(userFile.getFileSize()).append(",")
                    .append(userFile.getFileDir()).append("|&");
        }
    }

    // 删除指定文件列表中的文件
    private static void clearFileListInDir(int uid, List<UserFile> fileList) {
        for (UserFile file : fileList) {
            FileUtil.deleteFile(ConfigUtil.getUserFilePath() + "User" + uid + file.getFileDir() + file.getFileName());
        }
    }

    // 返回删除文件的总大小
    private static long getFileSizeSum(List<UserFile> fileList) {
        long fileSizeSum = 0;
        for (UserFile userFile : fileList) {
            fileSizeSum += userFile.getFileSize();
        }
        return fileSizeSum;
    }

    // =================提供给Servlet的接口
    public void initData() {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = getUser(user.getEmail());
        // 获得网盘数据
        PanData panData = getPanData(user.getUid());
        // 获得文件数据
        List<UserFile> fileList = getFileList(user.getUid(), "/");
        // 返回数据
        StringBuilder stringBuilder = new StringBuilder();
        // 添加用户属性
        addAttributes(stringBuilder, user, panData);
        // 添加文件列表
        addFileList(stringBuilder, fileList);
        // 告诉前端
        try {
            this.res.getWriter().println(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 上传文件
    public void uploadFile() {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = getUser(user.getEmail());
        PanData panData = getPanData(user.getUid());
        String path = ConfigUtil.getUserFilePath() + "User" + user.getUid();
        if (ServletFileUpload.isMultipartContent(req)) {
            FromMap fromMap = FromUtil.parseParam(req);
            // 前端传来的currentDir
            String fileDir = fromMap.getParamMap().get("currentDir");
            Map<String, FileItem> map = fromMap.getFileMap();
            for (String name : map.keySet()) {
                long fileSize = Math.round(map.get(name).getSize() * 1.0 / 1024 / 1024);
                // 保存在服务器上
                FileUtil.saveFile(map.get(name), path + fileDir + name);
                // 存储在数据库中
                saveUserFile(user.getUid(), name, '-', fileSize, 'Y', fileDir);
                // 增大当前存储
                synchronized (req.getSession()) {
                    long nowStorage = panData.getNowStorage() + fileSize;
                    setNowStorage(user.getUid(), nowStorage);
                }
            }
        }
    }

    // 下载文件
    public void downloadFile() {
        try {
            // 从session中获得user
            User user = SessionUtil.getUser(req);
            // 获得用户全部属性
            user = getUser(user.getEmail());
            String fileDir = req.getParameter("fileDir");
            String fileName = req.getParameter("fileName");
            // 得到要下载的文件
            File file = new File(ConfigUtil.getUserFilePath() + "User" + user.getUid() + fileDir + fileName);

            //如果文件不存在，则显示下载失败
            if (!file.exists()) {
                try {
                    PrintWriter writer = this.res.getWriter();
                    writer.println(Res.FALSE);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("下载失败");
            } else {
                // 设置相应头，控制浏览器下载该文件，这里就是会出现当你点击下载后，出现的下载地址框
                res.setContentType("application/octet-stream");
                res.addHeader("Content-Disposition", "attachment;filename=" + fileName + ";filename*=UTF-8");
                OutputStream os = res.getOutputStream();
                FileUtils.copyFile(file, os);
                os.flush();
                os.close();
                log.info("下载成功");
            }
        } catch (Exception e) {
            log.info("出现异常");
            e.printStackTrace();
        }
    }

    // 更新页面
    public void updateFileList() {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = getUser(user.getEmail());
        // 获得文件目录
        String fileDir = req.getParameter("fileDir");
        // 获得文件数据
        List<UserFile> fileList = getFileList(user.getUid(), fileDir);
        // 返回数据
        StringBuilder stringBuilder = new StringBuilder();
        addFileList(stringBuilder, fileList);
        try {
            PrintWriter writer = this.res.getWriter();
            writer.println(stringBuilder);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 删除指定文件
    public void deleteFile() {
        try {
            // 从session中获得user
            User user = SessionUtil.getUser(req);
            // 获得用户全部属性
            user = getUser(user.getEmail());
            int uid = user.getUid();
            String fileDir = req.getParameter("fileDir");
            String fileName = req.getParameter("fileName");
            // 设置UserFile中文件状态为N 表示删除 所以此时不释放空间
            deleteUserFile(uid, fileName, fileDir);
            // 告诉前端
            this.res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 查看垃圾箱
    public void lookupBin() {
        try {
            // 从session中获得user
            User user = SessionUtil.getUser(req);
            // 获得用户全部属性
            user = getUser(user.getEmail());
            int uid = user.getUid();
            // 查看垃圾箱前 删除过期文件
            PanData panData = getPanData(uid);
            int days = panData.getOutOfDate();
            List<UserFile> fileList = selectFilesOutOFfDateInDB(uid, days);
            // 删除磁盘中的过期文件
            clearFileListInDir(uid, fileList);
            // 删除数据库中的过期文件
            clearFilesOutOFfDateInDB(uid, days);
            // 查看垃圾箱
            fileList = lookupBin(uid);
            StringBuilder stringBuilder = new StringBuilder();
            addFileList(stringBuilder, fileList);
            try {
                PrintWriter writer = this.res.getWriter();
                writer.println(stringBuilder);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清空垃圾箱
    public void clearBin() {
        try {
            // 从session中获得user
            User user = SessionUtil.getUser(req);
            // 获得用户全部属性
            user = getUser(user.getEmail());
            int uid = user.getUid();
            // 添加Size
            PanData panData = getPanData(uid);
            List<UserFile> fileList = lookupBin(uid);
            long fileSizeSum = getFileSizeSum(fileList);
            long nowStorage = panData.getNowStorage() - fileSizeSum;
            setNowStorage(uid, nowStorage);
            // 清空回收站 数据库
            clearBin(uid);
            // 在磁盘上删除
            clearFileListInDir(uid, fileList);
            // 告诉前端
            this.res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清理垃圾箱中的某个文件
    public void clearUserFile() {
        try {
            // 从session中获得user
            User user = SessionUtil.getUser(req);
            // 获得用户全部属性
            user = getUser(user.getEmail());
            int uid = user.getUid();
            String fileDir = req.getParameter("fileDir");
            String fileName = req.getParameter("fileName");
            // 添加Size
            PanData panData = getPanData(uid);
            List<UserFile> fileList = getUserFile(uid, fileName, fileDir);
            long fileSizeSum = getFileSizeSum(fileList);
            // 通过所保证数据的正确性
            synchronized (req.getSession()) {
                long nowStorage = panData.getNowStorage() - fileSizeSum;
                setNowStorage(uid, nowStorage);
            }
            // 清空文件
            clearUserFile(uid, fileName, fileDir);
            // 在磁盘上删除
            clearFileListInDir(uid, fileList);
            // 告诉前端
            this.res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 还原垃圾箱中的某个文件
    public void recoveryFile() {
        try {
            // 从session中获得user
            User user = SessionUtil.getUser(req);
            // 获得用户全部属性
            user = getUser(user.getEmail());
            int uid = user.getUid();
            String fileDir = req.getParameter("fileDir");
            String fileName = req.getParameter("fileName");
            // 恢复文件
            recoveryFile(uid, fileName, fileDir);
            // 告诉前端
            this.res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 返回用户属性
    public void Attributes() {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得用户全部属性
        user = getUser(user.getEmail());
        // 获得网盘数据
        PanData panData = getPanData(user.getUid());
        StringBuilder stringBuilder = new StringBuilder();
        // 添加用户属性
        addAttributes(stringBuilder, user, panData);
        // 告诉前端
        try {
            this.res.getWriter().println(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 返回好友列表
    public void getFriend() {
        try {
            this.res.getWriter().println("好友系统, 没有完善");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 修改用户属性
    public void modifyAttributes() {
        // 从session中获得user
        User user = SessionUtil.getUser(req);
        // 获得要修改的参数
        String idCard = req.getParameter("idCard");
        String realName = req.getParameter("realName");
        String phone = req.getParameter("phone");
        modifyAttributes(user.getEmail(), idCard, realName, phone);
        try {
            this.res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

