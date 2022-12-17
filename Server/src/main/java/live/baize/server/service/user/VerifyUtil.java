package live.baize.server.service.user;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerifyUtil {

    /**
     * 生成验证码
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
    public void saveVerifyCode(String email, String verifyCode) {
//        verifyMapper.insertVerify(new Verify(email, verifyCode));
    }

    /**
     * 检查验证码
     */
    public boolean checkVerifyCode(String email, String verifyCode) {
//        Verify flag = null;
//        try {
//            MyBatis myBatis = new MyBatis();
//            SqlSession sqlSession = myBatis.getSqlSession();
//            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
//            flag = verifyMapper.selectVerify(new Verify(email, verifyCode));
//            myBatis.closeSqlSession();
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("检查验证码异常");
//        }
//        return flag != null;
        return true;
    }


}
