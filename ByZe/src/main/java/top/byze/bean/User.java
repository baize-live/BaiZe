package top.byze.bean;

public class User {
    private String uid;
    private String username;
    private final String password;
    private final String email;
    private String phone;
    private String IDCard;
    private String realName;
    private String isOpenYou;
    private String isOpenPan;
    private String createTime;

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getRealName() {
        return realName;
    }

    public String getIsOpenPan() {
        return isOpenPan;
    }

    public String getIsOpenYou() {
        return isOpenYou;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", IDCard='" + IDCard + '\'' +
                ", realName='" + realName + '\'' +
                ", isOpenYou='" + isOpenYou + '\'' +
                ", isOpenPan='" + isOpenPan + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

