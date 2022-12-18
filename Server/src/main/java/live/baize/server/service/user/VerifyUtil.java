package live.baize.server.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import live.baize.server.bean.business.Verify;
import live.baize.server.mapper.user.VerifyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

@Service
public class VerifyUtil {

    @Resource
    VerifyMapper verifyMapper;

    /**
     * 生成验证码
     *
     * @return String 6位数字验证码
     */
    public String generateVerifyCode() {
        final int size = 6;
        Random random = new Random();
        char[] chars = ("0123456789").toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            int num = random.nextInt(chars.length);
            char c = chars[num];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * 保存验证码
     */
    public boolean saveVerifyCode(String email, String verifyCode) {
        return verifyMapper.insert(
                new Verify(email, verifyCode)
        ) == 1;
    }

    /**
     * 检查验证码
     */
    public boolean checkVerifyCode(String email, String verifyCode) {
        return verifyMapper.selectOne(
                new QueryWrapper<Verify>()
                        .eq("email", email)
                        .eq("verifyCode", verifyCode)
                        .select("vid")
        ) != null;
    }

}
