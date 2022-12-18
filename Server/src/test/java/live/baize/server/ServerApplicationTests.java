package live.baize.server;

import live.baize.server.service.user.UserUtil;
import live.baize.server.service.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ServerApplicationTests {
    @Resource
    MailUtil mailUtil;
    @Resource
    UserUtil userUtil;

    @Test
    void testMailUtil() {
        System.out.println("======================== start testMailUtil ========================");
        try {
            mailUtil.sendVerifyCode("baize_live@163.com", "123456");
            System.out.println("Success sendVerifyCode");
            mailUtil.sendKeepAliveMail(0);
            System.out.println("Success sendKeepAliveMail");
            mailUtil.sendExceptionMail("test sendExceptionMail");
            System.out.println("Success sendExceptionMail");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("======================== end   testMailUtil ========================");
    }

    @Test
    void testUserUtil() {
        String email = "12345@baize.com";
        String username = "Test";
        String password = "Test";
        System.out.println("======================== start testUserUtil ========================");
        System.out.println("checkEmail: " + userUtil.checkEmail(email));
        System.out.println("addUser: " + userUtil.addUser(username, password, email));
        System.out.println("checkEmail: " + userUtil.checkEmail(email));
        System.out.println("findUser: " + userUtil.findUser(email, password));
        System.out.println("delUser: " + userUtil.delUser(email));
        System.out.println("checkEmail: " + userUtil.checkEmail(email));
        System.out.println("======================== end   testUserUtil ========================");
    }

}
