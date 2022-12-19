package live.baize.server.bean.business;

import lombok.Getter;

@Getter
public class Verify {
    private Integer vid;
    private String email;
    private String verifyCode;
    private String createTime;

    public Verify(Integer vid) {
        this.vid = vid;
    }

    public Verify(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
