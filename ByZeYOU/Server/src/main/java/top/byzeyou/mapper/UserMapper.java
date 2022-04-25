package top.byzeyou.mapper;

import top.byzeyou.bean.User;

import java.util.List;

public interface UserMapper {
    // 获得全部用户数据
    List<User> selectAll();

    // 添加用户
    void addUser(User user);
}
