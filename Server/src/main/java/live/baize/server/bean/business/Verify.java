package live.baize.server.bean.business;

/**
 * @author CodeXS
 */
public class Verify {
    private final String email;
    private final String verifyCode;
    private Integer vid;
    private String createTime;

    public Verify(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }

    public String getEmail() {
        return email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }
}
