package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.User;

import java.util.List;

public interface UserMapper {
    List<User> selectAll();

    User getUser(@Param("email") String email);

    int getUid(@Param("email") String email);

    boolean isOpenPan(@Param("email") String email);

    boolean isOpenYou(@Param("email") String email);

    void openPan(@Param("email") String email);

    void openYou(@Param("email") String email);

    boolean checkEmail(@Param("email") String email);

    boolean findUser(@Param("email") String email, @Param("password") String password);

    void addUser(@Param("username") String username, @Param("password") String password, @Param("email") String email);
}
