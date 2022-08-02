package top.byze.mapper;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author CodeXS
 * VerifyMapper 接口中使用
 */
public class VerifySqlProvider {
    public static String selectVerify() {
        return new SQL() {
            {
                SELECT("*");
                FROM("verify");
                WHERE("email = #{email} and verifyCode = #{verifyCode}");
            }
        }.toString();
    }

    public static String insertVerify() {
        return new SQL() {
            {
                INSERT_INTO("verify");
                VALUES("email, verifyCode", "#{email}, #{verifyCode}");
            }
        }.toString();
    }
}
