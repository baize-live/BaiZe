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
     * 获得所有用户
     *
     * @return List<User> 用户列表
     */
    @SelectProvider(type = User.class, method = "selectAll")
    List<User> selectAll();

    /**
     * 获得指定用户
     *
     * @param user 用户
     * @return User 用户
     */
    @SelectProvider(type = User.class, method = "selectUser")
    User selectUser(User user);

    /**
     * 设置用户属性
     *
     * @param user 用户
     */
    @UpdateProvider(type = User.class, method = "updateUser")
    void updateUser(User user);

    /**
     * 插入新的用户
     *
     * @param user 用户
     */
    @InsertProvider(type = User.class, method = "insertUser")
    void insertUser(User user);

}
