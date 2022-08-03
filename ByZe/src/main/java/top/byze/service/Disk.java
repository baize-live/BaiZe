package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.PanData;
import top.byze.bean.User;
import top.byze.bean.UserFile;
import top.byze.mapper.PanDataMapper;
import top.byze.mapper.UserFileMapper;
import top.byze.mapper.UserFileSqlProvider;
import top.byze.mapper.UserMapper;
import top.byze.utils.ConfigUtil;
import top.byze.utils.FileUtil;
import top.byze.utils.MyBatis;

import java.util.List;

/**
 * @author CodeXS
 */
@Slf4j
public class Disk {
    // ========================================与数据库交互=============================================== //

    /**
     * 从数据库中获得指定用户
     *
     * @param email 用户邮箱
     * @return 用户
     */
    public static User getUser(String email) {
        User user = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.selectUser(new User(email));
            if (!userList.isEmpty()) {
                user = userList.get(0);
            }
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得用户数据异常");
        }
        return user;
    }

    /**
     * 从数据库中获得网盘数据
     *
     * @param uid 用户ID
     * @return 网盘数据
     * *
     */
    public static PanData getPanData(Integer uid) {
        PanData panData = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            List<PanData> panDataList = panDataMapper.selectPanData(new PanData(uid));
            if (!panDataList.isEmpty()) {
                panData = panDataList.get(0);
            }
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得网盘数据异常");
        }
        return panData;
    }

    /**
     * 获得指定目录下的文件链表
     *
     * @param uid     用户ID
     * @param fileDir 目录
     * @return 文件链表
     */
    public static List<UserFile> getFileList(Integer uid, String fileDir) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.selectUserFile(new UserFile().setUid(uid).setFileDir(fileDir).setSelectConditionTypeId(UserFileSqlProvider.Select_By_UID_FileDir_FileState_Y));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得指定目录下的文件链表异常");
        }
        return fileList;
    }

    /**
     * 从数据库中获得指定文件
     *
     * @param uid      用户ID
     * @param fileName 文件名
     * @param fileDir  目录
     * @return 文件
     */
    public static List<UserFile> getUserFile(Integer uid, String fileName, String fileDir) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.selectUserFile(new UserFile(uid, fileName, fileDir));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查看指定文件异常");
        }
        return fileList;
    }

    /**
     * 查看回收站的所有文件
     *
     * @param uid 用户ID
     * @return 文件链表
     */
    public static List<UserFile> lookupBin(Integer uid) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.selectUserFile(new UserFile().setUid(uid).setSelectConditionTypeId(UserFileSqlProvider.Select_By_UID_FileState_N));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查看回收站的所有文件异常");
        }
        return fileList;
    }

    /**
     * 查找回收站中过期的文件
     *
     * @param uid  用户ID
     * @param days 过期天数
     * @return 文件链表
     */
    public static List<UserFile> selectFilesOutOfDateInDatabase(Integer uid, Integer days) {
        List<UserFile> fileList = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.selectFilesOutOfDateInDatabase(uid, days);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查找数据库中的过期文件异常");
        }
        return fileList;
    }

    /**
     * 保存用户上传文件
     *
     * @param uid      用户ID
     * @param fileName 文件名
     * @param fileDir  目录
     * @param fileSize 文件大小
     * @param fileType 文件类型
     */
    public static void saveUserFile(Integer uid, String fileName, String fileType, Integer fileSize, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.insertUserFile(new UserFile().setUid(uid).setFileName(fileName).setFileDir(fileDir).setFileSize(fileSize).setFileType(fileType));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存用户上传文件异常");
        }
    }

    /**
     * 设置当前存储
     *
     * @param uid        用户ID
     * @param nowStorage 当前存储
     */
    public static void setNowStorage(Integer uid, Integer nowStorage) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panDataMapper.updatePanData(new PanData(uid).setNowStorage(nowStorage));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("设置当前存储异常");
        }
    }

    /**
     * 删除指定文件(修改标记而已, 即放到回收站)
     *
     * @param uid      用户ID
     * @param fileName 文件名
     * @param fileDir  目录
     */
    public static void deleteUserFile(Integer uid, String fileName, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.updateUserFile(new UserFile(uid, fileName, fileDir).setFileState("N"));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除指定文件异常");
        }
    }

    /**
     * 清空回收站
     *
     * @param uid 用户ID
     */
    public static void clearBin(Integer uid) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.deleteUserFile(new UserFile().setUid(uid).setDeleteConditionTypeId(UserFileSqlProvider.Delete_By_UID_FileState_N));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清空回收站异常");
        }
    }

    /**
     * 清除指定文件
     *
     * @param uid      用户ID
     * @param fileName 文件名
     * @param fileDir  目录
     */
    public static void clearUserFile(Integer uid, String fileName, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.deleteUserFile(new UserFile(uid, fileName, fileDir));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清除指定文件异常");
        }
    }

    /**
     * 恢复指定文件
     *
     * @param uid      用户ID
     * @param fileName 文件名
     * @param fileDir  目录
     */
    public static void recoveryFile(Integer uid, String fileName, String fileDir) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.updateUserFile(new UserFile(uid, fileName, fileDir).setFileState("Y"));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("恢复指定文件异常");
        }
    }

    /**
     * 清除数据库中的过期文件 这个时间可以个性化设置
     *
     * @param uid  用户ID
     * @param days 过期天数
     */
    public static void clearFilesOutOfDateInDatabase(Integer uid, Integer days) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            userFileMapper.clearFilesOutOfDateInDatabase(uid, days);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清除数据库中的过期文件异常");
        }
    }

    /**
     * 修改用户属性
     *
     * @param email    用户邮箱
     * @param phone    用户手机号
     * @param idCard   用户身份证号
     * @param realName 用户真实姓名
     */
    public static void modifyAttributes(String email, String idCard, String realName, String phone) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.updateUser(new User(email).setIdCard(idCard).setRealName(realName).setPhone(phone));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改用户属性异常");
        }
    }

    // ============================================功  能================================================ //

    /**
     * 生成用户属性字符串
     *
     * @param stringBuilder 字符串拼接器
     * @param user          用户
     * @param panData       用户网盘信息
     */
    public static void addAttributes(StringBuilder stringBuilder, User user, PanData panData) {
        stringBuilder
                .append("username=").append(user.getUsername()).append("&")
                .append("uid=").append(user.getUid()).append("&")
                .append("email=").append(user.getEmail()).append("&")
                .append("idCard=").append(user.getIdCard() == null ? " " : user.getIdCard()).append("&")
                .append("realName=").append(user.getRealName() == null ? " " : user.getRealName()).append("&")
                .append("phone=").append(user.getPhone() == null ? " " : user.getPhone()).append("&")
                .append("icon=").append(panData.getIcon()).append("&")
                .append("nowStorage=").append(panData.getNowStorage()).append("&")
                .append("maxStorage=").append(panData.getMaxStorage()).append("&")
                .append("grade=").append(panData.getGrade()).append("&");
    }

    /**
     * 生成文件信息字符串
     *
     * @param stringBuilder 字符串拼接器
     * @param fileList      文件列表
     */
    public static void addFileList(StringBuilder stringBuilder, List<UserFile> fileList) {
        for (UserFile userFile : fileList) {
            stringBuilder
                    .append("file=")
                    .append(userFile.getFileName()).append(",")
                    .append(userFile.getFileSize()).append(",")
                    .append(userFile.getFileType()).append(",")
                    .append(userFile.getFileDir()).append(",")
                    .append(userFile.getFileState()).append(",")
                    .append(userFile.getDeleteTime()).append("|&")
            ;
        }
    }

    /**
     * 删除指定文件列表中的文件
     *
     * @param uid      用户ID
     * @param fileList 文件列表
     */
    public static void clearFileListInDir(Integer uid, List<UserFile> fileList) {
        for (UserFile file : fileList) {
            FileUtil.deleteFile(ConfigUtil.getUserFilePath() + "User" + uid + file.getFileDir() + file.getFileName());
        }
    }

    /**
     * 返回删除文件的总大小
     *
     * @param fileList 文件列表
     * @return 总大小
     */
    public static Integer getFileSizeSum(List<UserFile> fileList) {
        Integer fileSizeSum = 0;
        for (UserFile userFile : fileList) {
            fileSizeSum += userFile.getFileSize();
        }
        return fileSizeSum;
    }
}

