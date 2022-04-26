package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.User;

import java.util.List;

public interface UserMapper {

    void delAll();

    List<User> selectAll();

    boolean checkEmail(@Param("email") String email);

    boolean findUser(@Param("email") String email, @Param("password") String password);

    void addUser(@Param("username") String username, @Param("password") String password, @Param("email") String email);

}
