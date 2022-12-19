package live.baize.server.service.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {
    static final String String_Seed_ASCII = "!\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`ABCDEFGHIJKLMNOPQRSTUVWXYZ{|}~";
    static final String String_Seed_Number = "0123456789";

    private String generateRandomString(String StringSeed, int size) {
        Random random = new Random();
        char[] chars = (StringSeed).toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            int num = random.nextInt(chars.length);
            char c = chars[num];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * 生成验证码
     *
     * @return String 6位数字验证码
     */
    public String generateVerifyCode() {
        return generateRandomString(String_Seed_Number, 6);
    }

    public String generatePasswdSalt() {
        return generateRandomString(String_Seed_ASCII, 16);
    }
}
