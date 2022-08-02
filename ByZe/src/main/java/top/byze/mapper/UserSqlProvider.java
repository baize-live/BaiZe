package top.byze.mapper;

import org.apache.ibatis.jdbc.SQL;
import top.byze.bean.User;

/**
 * @author CodeXS
 * UserMapper 接口中使用
 */
public class UserSqlProvider {
    public static String selectUser(User user) {
        return new SQL() {
            {
                SELECT("*");
                FROM("user");
                if (user.getSelectConditionTypeId() != null) {
                    // 目前只有 通过邮箱查询 在可预见的未来 也只会通过邮箱
                    WHERE("email = #{email}");
                } else {
                    // 默认通过 email 查询
                    WHERE("email = #{email}");
                }
            }
        }.toString();
    }

    public static String updateUser(User user) {
        return new SQL() {
            {
                UPDATE("user");
                if (user.getUid() != null) {
                    SET("uid = #{uid}");
                }
                if (user.getEmail() != null) {
                    SET("email = #{email}");
                }
                if (user.getPhone() != null) {
                    SET("phone = #{phone}");
                }
                if (user.getIdCard() != null) {
                    SET("idCard = #{idCard}");
                }
                if (user.getUsername() != null) {
                    SET("username = #{username}");
                }
                if (user.getPassword() != null) {
                    SET("password = #{password}");
                }
                if (user.getRealName() != null) {
                    SET("realName = #{realName}");
                }
                if (user.getIsOpenPan() != null) {
                    SET("isOpenPan = #{isOpenPan}");
                }
                if (user.getIsOpenYou() != null) {
                    SET("isOpenYou = #{isOpenYou}");
                }
                WHERE("email = #{email}");
            }
        }.toString();
    }

    public static String insertUser() {
        return new SQL() {
            {
                INSERT_INTO("user");
                VALUES("email", "#{email}");
            }
        }.toString();
    }
}
