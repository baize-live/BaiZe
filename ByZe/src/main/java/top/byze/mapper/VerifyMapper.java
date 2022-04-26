package top.byze.mapper;

import org.apache.ibatis.annotations.Param;

public interface VerifyMapper {

    boolean checkVerifyCode(@Param("email") String email, @Param("verifyCode") String verifyCode);

    void saveVerifyCode(@Param("email") String email, @Param("verifyCode") String verifyCode);
}
