package live.baize.server.bean.business;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private Integer uid;
    private String password;
    private final String email;
    private String username;
    private String phone;
    private String idCard;
    private String realName;
    private String isOpenGame;
    private String isOpenDisk;
    private String createTime;

    // 构造函数 email 为唯一标识
    public User(String email) {
        // TODO: 添加一个判断 @
        this.email = email;
    }

}

