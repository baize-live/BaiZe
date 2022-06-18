package top.byze.bean;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author CodeXS
 */
public class Verify {
    private final String email;
    private final String verifyCode;
    private Integer vid;
    private String createTime;

    public Verify(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }

    /**
     * 以下两个函数 VerifyMapper 接口中使用
     */
    public static String selectVerify(Verify verify) {
        return new SQL() {
            {
                SELECT("*");
                FROM("verify");
                WHERE("email = #{email} and verifyCode = #{verifyCode}");
            }
        }.toString();
    }

    public static String insertVerify(Verify verify) {
        return new SQL() {
            {
                INSERT_INTO("verify");
                VALUES("email, verifyCode", "#{email}, #{verifyCode}");
            }
        }.toString();
    }

    public String getEmail() {
        return email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

}
