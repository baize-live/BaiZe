package live.baize.server;

import live.baize.server.service.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ServerApplicationTests {
    @Resource
    MailUtil mailUtil;

    @Test
    void testMailUtil() {
        try {
            mailUtil.sendVerifyCode("baize_live@163.com", "123456");
            System.out.println("Test Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
