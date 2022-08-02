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
     * 查询用户文件
     */
    @SelectProvider(type = UserFileSqlProvider.class, method = "selectUserFile")
    List<UserFile> selectUserFile(UserFile userFile);

    /**
     * 插入用户文件
     */
    @InsertProvider(type = UserFileSqlProvider.class, method = "insertUserFile")
    void insertUserFile(UserFile userFile);

    /**
     * 更新用户文件
     */
    @UpdateProvider(type = UserFileSqlProvider.class, method = "updateUserFile")
    void updateUserFile(UserFile userFile);

    /**
     * 删除用户文件
     */
    @DeleteProvider(type = UserFileSqlProvider.class, method = "deleteUserFile")
    void deleteUserFile(UserFile userFile);

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
     * 清理过期文件
     *
     * @param uid  用户
     * @param days 时限
     */
    @Delete("delete from UserFile where fileState = 'N' and deleteTime < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL #{days} day);")
    void clearFilesOutOfDateInDatabase(@Param("UID") int uid, @Param("days") int days);
}
