package top.byze.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import top.byze.bean.User;

import java.util.List;

/**
 * @author CodeXS
 * <p>
 * 用户表的映射器
 */
public interface UserMapper {
    /**
     * 查询用户
     */
    @SelectProvider(type = UserSqlProvider.class, method = "selectUser")
    List<User> selectUser(User user);

    /**
     * 更新用户
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "updateUser")
    void updateUser(User user);

    /**
     * 插入用户
     */
    @InsertProvider(type = UserSqlProvider.class, method = "insertUser")
    void insertUser(User user);
}
