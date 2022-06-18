package top.byze.mapper;

import org.apache.ibatis.annotations.*;
import top.byze.bean.UserFile;

import java.util.List;

/**
 * @author CodeXS
 * <p>
 * 用户文件表的映射器
 */
public interface UserFileMapper {

    /**
     * 拿到 用户指定文件夹下的所有文件
     *
     * @param uid     用户id
     * @param fileDir 目录
     * @return List<UserFile> 文件列表
     */
    @Select("select * from userFile where UID = #{UID} and fileDir = #{fileDir} and fileState = 'Y';")
    List<UserFile> getFileList(@Param("UID") int uid, @Param("fileDir") String fileDir);

    /**
     * 拿到 指定文件 以列表形式返回
     *
     * @param uid      用户
     * @param fileDir  目录
     * @param fileName 文件名
     * @return List<UserFile> 文件列表
     */
    @Select("select * from userFile where UID = #{UID} and fileDir = #{fileDir} and fileName = #{fileName}")
    List<UserFile> getUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 拿到 用户所有回收站里的文件
     *
     * @param uid 用户
     * @return List<UserFile> 文件列表
     */
    @Select("select * from userFile where UID = #{UID} and fileState = 'N';")
    List<UserFile> lookupBin(@Param("UID") int uid);

    /**
     * 拿到 用户所有已经过期的文件
     *
     * @param uid  用户
     * @param days 时限
     * @return List<UserFile> 文件列表
     */
    @Select("select * from UserFile where fileState = 'N' and deleteTime < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL #{days} day);")
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
    @Insert("insert into userFile(UID, fileName, fileType, fileSize, fileState, fileDir) values (#{UID}, #{fileName}, #{fileType}, #{fileSize}, #{fileState}, #{fileDir});")
    void saveUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileType") char fileType, @Param("fileSize") long fileSize, @Param("fileState") char fileState, @Param("fileDir") String fileDir);

    /**
     * 删除用户文件
     * 修改 fileState 为 N, 并不是真正的删除
     *
     * @param uid      用户
     * @param fileDir  文件夹
     * @param fileName 文件名
     */
    @Update("update userFile set fileState = 'N' where UID = #{UID} and fileName = #{fileName} and fileDir = #{fileDir};")
    void deleteUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 恢复用户文件
     * 修改 fileState 为 Y
     *
     * @param uid      用户
     * @param fileDir  文件夹
     * @param fileName 文件名
     */
    @Update("update userFile set fileState = 'Y' where UID = #{UID} and fileName = #{fileName} and fileDir = #{fileDir};")
    void recoveryFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 清理过期文件
     *
     * @param uid  用户
     * @param days 时限
     */
    @Delete("delete from UserFile where fileState = 'N' and deleteTime < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL #{days} day);")
    void clearFilesOutOfDateInDatabase(@Param("UID") int uid, @Param("days") int days);

    /**
     * 清理指定文件
     *
     * @param uid      用户
     * @param fileName 文件名
     * @param fileDir  文件目录
     */
    @Delete("delete from userFile where UID = #{UID} and fileName = #{fileName} and fileDir = #{fileDir};")
    void clearUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    /**
     * 清空回收站
     *
     * @param uid 用户
     */
    @Delete("delete from userFile where UID = #{UID} and fileState = 'N';")
    void clearBin(@Param("UID") int uid);

}
