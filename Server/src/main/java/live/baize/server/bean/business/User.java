package live.baize.server.bean.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("User")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer UId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String idCard;
    private String realName;
    private String passwdSalt;
    private String isOpenGame;
    private String isOpenDisk;
    private Date createTime;

    public User(String email) {
        this.email = email;
    }

}

