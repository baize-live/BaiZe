package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.UserFile;

import java.util.List;

/**
 * @author CodeXS
 */
public interface UserFileMapper {

    /**
     * 拿到 用户指定文件夹下的所有文件
     *
     * @param uid     用户id
     * @param fileDir 目录
     * @return List<UserFile> 文件列表
     */
    List<UserFile> getFileList(@Param("UID") int uid, @Param("fileDir") String fileDir);

    /**
     * 拿到 指定文件 以列表形式返回
     *
     * @param uid      用户
     * @param fileDir  目录
     * @param fileName 文件名
     * @return List<UserFile> 文件列表
     */
    List<UserFile> getUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 拿到 用户所有回收站里的文件
     *
     * @param uid 用户
     * @return List<UserFile> 文件列表
     */
    List<UserFile> lookupBin(@Param("UID") int uid);

    /**
     * 拿到 用户所有超出指定时间的文件
     *
     * @param uid  用户
     * @param days 时限
     * @return List<UserFile> 文件列表
     */
    List<UserFile> selectFilesOutOfDateInDatabase(@Param("UID") int uid, @Param("days") int days);

    /**
     * 保存用户文件
     *
     * @param uid       用户
     * @param fileDir   文件夹
     * @param fileName  文件名
     * @param fileSize  文件大小
     * @param fileType  文件类型
     * @param fileState 文件状态
     */
    void saveUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileType") char fileType, @Param("fileSize") long fileSize, @Param("fileState") char fileState, @Param("fileDir") String fileDir);

    /**
     * 删除用户文件, 修改 fileState 为 N, 并不是真正的删除
     *
     * @param uid      用户
     * @param fileDir  文件夹
     * @param fileName 文件名
     */
    void deleteUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 恢复用户文件, 修改 fileState 为 Y
     *
     * @param uid      用户
     * @param fileDir  文件夹
     * @param fileName 文件名
     */
    void recoveryFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 彻底删除过期文件
     *
     * @param uid  用户
     * @param days 时限
     */
    void clearFilesOutOfDateInDatabase(@Param("UID") int uid, @Param("days") int days);

    /**
     * 清空用户回收站
     *
     * @param uid 用户
     */
    void clearBin(@Param("UID") int uid);

    /**
     * 清理用户指定文件
     *
     * @param uid      用户
     * @param fileName 文件名
     * @param fileDir  文件目录
     */
    void clearUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

}
