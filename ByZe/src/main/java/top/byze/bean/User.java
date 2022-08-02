package top.byze.bean;

import lombok.Getter;

/**
 * @author CodeXS
 */
@Getter
public class User {
    private Integer uid;
    private String password;
    private final String email;
    private String username;
    private String phone;
    private String idCard;
    private String realName;
    private String isOpenYou;
    private String isOpenPan;
    private String createTime;

    // 非数据库字段 用于生成不同的sql语句
    private Integer selectConditionTypeId;

    // 构造函数 email 为唯一标识
    public User(String email) {
        // TODO: 添加一个判断 @
        this.email = email;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public User setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public User setIsOpenPan(String isOpenPan) {
        this.isOpenPan = isOpenPan;
        return this;
    }

    public User setIsOpenYou(String isOpenYou) {
        this.isOpenYou = isOpenYou;
        return this;
    }

    public User setSelectConditionTypeId(Integer selectConditionTypeId) {
        this.selectConditionTypeId = selectConditionTypeId;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", realName='" + realName + '\'' +
                ", isOpenYou='" + isOpenYou + '\'' +
                ", isOpenPan='" + isOpenPan + '\'' +
                ", createTime='" + createTime + '\'' +
                ", selectConditionTypeId=" + selectConditionTypeId +
                '}';
    }
}

