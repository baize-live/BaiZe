package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.User;

import java.util.List;

public interface UserMapper {
    List<User> selectAll();

    void addUser(User user);

    void delAll();

    boolean checkEmail(String email);

    boolean findUser(@Param("email") String email, @Param("password") String password);
}
