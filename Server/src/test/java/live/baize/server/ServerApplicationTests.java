package live.baize.server;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import live.baize.server.bean.business.disk.DiskData;
import live.baize.server.mapper.disk.DiskDataMapper;
import live.baize.server.mapper.user.UserMapper;
import live.baize.server.mapper.user.VerifyMapper;
import live.baize.server.service.user.UserUtil;
import live.baize.server.service.utils.MailUtil;
import live.baize.server.service.utils.PasswdUtil;
import live.baize.server.service.utils.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ServerApplicationTests {
    @Resource
    MailUtil mailUtil;
    @Resource
    UserUtil userUtil;
    @Resource
    RandomUtil randomUtil;
    @Resource
    PasswdUtil passwdUtil;

    final String email = "baize_live@163.com";

    @Test
    void testUserUtil() {
        String username = "TestUserUtil";
        String password = "TestUserUtil";
        String code = randomUtil.generateVerifyCode();
        System.out.println("======================== start testUserUtil ========================");
        System.out.println("checkEmailIsRegister: " + userUtil.checkEmailIsRegister(email));
        System.out.println("addUser: " + userUtil.addUser(username, password, email));
        System.out.println("checkEmailIsRegister: " + userUtil.checkEmailIsRegister(email));
        System.out.println("findUser: " + userUtil.findUser(email, password));
        System.out.println("getUserBasicInfoByEmail: " + userUtil.getUserBasicInfoByEmail(email));
        System.out.println("getUserIdByEmail: " + userUtil.getUserIdByEmail(email));
        System.out.println("delUser: " + userUtil.delUser(email));
        System.out.println("checkEmailIsRegister: " + userUtil.checkEmailIsRegister(email));
        System.out.println("checkVerifyCode: " + userUtil.checkVerifyCode(email, code));
        System.out.println("saveVerifyCode: " + userUtil.saveVerifyCode(email, code));
        System.out.println("checkVerifyCode: " + userUtil.checkVerifyCode(email, code));
        System.out.println("======================== end   testUserUtil ========================");
    }

    @Test
    void testMailUtil() {
        System.out.println("======================== start testMailUtil ========================");
        try {
            mailUtil.sendVerifyCode(email, "123456");
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
    void testRandomUtil() {
        System.out.println("======================== start testRandomUtil ========================");
        System.out.println("generateVerifyCode: " + randomUtil.generateVerifyCode());
        System.out.println("generatePasswdSalt: " + randomUtil.generatePasswdSalt());
        System.out.println("======================== end   testRandomUtil ========================");
    }

    @Test
    void testPasswdUtil() {
        System.out.println("======================== start testPasswdUtil ========================");
        String passwdSalt = randomUtil.generatePasswdSalt();
        System.out.println(passwdUtil.generateFileName(1, "/", "test.jpg"));
        System.out.println(passwdUtil.generatePassword("SS111827jj!", passwdSalt));
        System.out.println("======================== end   testPasswdUtil ========================");
    }

    @Resource
    UserMapper userMapper;
    @Resource
    VerifyMapper verifyMapper;
    @Resource
    DiskDataMapper diskDataMapper;

    @Test
    void databaseScript() {

    }
}
