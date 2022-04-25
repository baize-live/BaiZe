package top.byze.bean;

public class User {
    private String uid;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String nowStorage;
    private String maxStorage;
    private String createTime;

    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNowStorage() {
        return nowStorage;
    }

    public void setNowStorage(String nowStorage) {
        this.nowStorage = nowStorage;
    }

    public String getMaxStorage() {
        return maxStorage;
    }

    public void setMaxStorage(String maxStorage) {
        this.maxStorage = maxStorage;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", nowStorage='" + nowStorage + '\'' +
                ", maxStorage='" + maxStorage + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

