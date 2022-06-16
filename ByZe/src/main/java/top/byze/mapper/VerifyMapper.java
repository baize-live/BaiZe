package top.byze.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author CodeXS
 */
public interface VerifyMapper {

    /**
     * 检查验证码是否正确
     *
     * @param email      注册邮箱
     * @param verifyCode 验证码
     * @return 返回bool
     */
    boolean checkVerifyCode(@Param("email") String email, @Param("verifyCode") String verifyCode);

    /**
     * 保存生成的验证码
     *
     * @param email      注册邮箱
     * @param verifyCode 验证码
     */
    void saveVerifyCode(@Param("email") String email, @Param("verifyCode") String verifyCode);
}
