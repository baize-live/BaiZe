package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.bean.Verify;
import top.byze.mapper.UserMapper;
import top.byze.mapper.VerifyMapper;
import top.byze.utils.MyBatis;

import java.util.Random;

/**
 * @author CodeXS
 */
@Slf4j
public class Register {
    // ========================================与数据库交互=============================================== //

    /**
     * 检查邮箱是否已经注册
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = !userMapper.selectUser(new User(email)).isEmpty();
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("检查邮箱异常");
        }
        return flag;
    }

    /**
     * 保存验证码
     */
    public static void saveVerifyCode(String email, String verifyCode) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
            verifyMapper.insertVerify(new Verify(email, verifyCode));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存验证码异常");
        }
    }

    /**
     * 检查验证码
     */
    public static boolean checkVerifyCode(String email, String verifyCode) {
        Verify flag = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
            flag = verifyMapper.selectVerify(new Verify(email, verifyCode));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("检查验证码异常");
        }
        return flag != null;
    }

    /**
     * 添加用户
     */
    public static void addUser(String username, String password, String email) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = new User(email);
            userMapper.insertUser(user);
            userMapper.updateUser(user.setUsername(username).setPassword(password));
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加用户异常");
        }
    }

    // ============================================功  能================================================ //

    /**
     * 生成验证码
     */
    public static String generateVerifyCode() {
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
}
