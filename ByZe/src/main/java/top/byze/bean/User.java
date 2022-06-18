package top.byze.bean;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author CodeXS
 */
public class User {
    private Integer uid;
    private String password;
    private String email;
    private String username;
    private String phone;
    private String idCard;
    private String realName;
    private String isOpenYou;
    private String isOpenPan;
    private String createTime;

    public User(String email) {
        this.email = email;
    }

    /**
     * 以下四个函数 UserMapper 接口中使用
     */
    public static String selectAll() {
        return new SQL() {
            {
                SELECT("*");
                FROM("`user`");
            }
        }.toString();
    }

    public static String selectUser(User user) {
        return new SQL() {
            {
                SELECT("*");
                FROM("user");
                WHERE("email = #{email}");
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

    public static String insertUser(User user) {
        return new SQL() {
            {
                INSERT_INTO("user");
                VALUES("email", "#{email}");
            }
        }.toString();
    }

    public Integer getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }

    public User setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public User setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getIsOpenPan() {
        return isOpenPan;
    }

    public User setIsOpenPan(String isOpenPan) {
        this.isOpenPan = isOpenPan;
        return this;
    }

    public String getIsOpenYou() {
        return isOpenYou;
    }

    public User setIsOpenYou(String isOpenYou) {
        this.isOpenYou = isOpenYou;
        return this;
    }

}

