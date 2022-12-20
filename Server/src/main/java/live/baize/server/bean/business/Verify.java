package live.baize.server.bean.business;

import lombok.Getter;

import java.util.Date;

@Getter
public class Verify {
    private Integer VId;
    private String email;
    private String verifyCode;
    private Date createTime;

    public Verify(Integer VId) {
        this.VId = VId;
    }

    public Verify(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
