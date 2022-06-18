package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.bean.Verify;
import top.byze.mapper.UserMapper;
import top.byze.mapper.VerifyMapper;
import top.byze.utils.MailUtil;
import top.byze.utils.MyBatis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * @author CodeXS
 */
@Slf4j
public class Register {
    final HttpServletRequest req;
    final HttpServletResponse res;

    public Register(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    // ========================================与数据库交互=============================================== //

    /**
     * 检查邮箱是否已经注册
     */
    private static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.selectUser(new User(email)) != null;
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查邮箱异常");
        }
        return flag;
    }

    /**
     * 保存验证码
     */
    private static void saveVerifyCode(String email, String verifyCode) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
            verifyMapper.insertVerify(new Verify(email, verifyCode));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("保存验证码异常");
        }
    }

    /**
     * 检查验证码
     */
    private static boolean checkVerifyCode(String email, String verifyCode) {
        Verify flag = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
            flag = verifyMapper.selectVerify(new Verify(email, verifyCode));
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查验证码异常");
        }
        return flag != null;
    }

    /**
     * 添加用户
     */
    private static void addUser(String username, String password, String email) {
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
            log.error("添加用户异常");
        }
    }

    // ============================================功  能================================================ //

    /**
     * 生成验证码
     */
    private static String generateVerifyCode() {
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

    // ========================================Servlet接口================================================= //

    /**
     * 检查邮箱
     */
    public void checkEmail() {
        try {
            String email = this.req.getParameter("email");
            // 判断是否存在此邮箱
            if (checkEmail(email)) {
                this.res.getWriter().println(Res.FALSE);
                log.info("此邮箱已被注册");
            } else {
                this.res.getWriter().println(Res.TRUE);
                log.info("此邮箱未被注册");
            }
        } catch (Exception e) {
            log.error("这里有异常");
        }
    }

    /**
     * 发送验证码
     */
    public void sendVerifyCode() {
        // 获取注册码
        String email = this.req.getParameter("email");
        String verifyCode = generateVerifyCode();
        // 保存验证码
        saveVerifyCode(email, verifyCode);
        // 发送邮件
        try {
            new MailUtil(email, verifyCode).sendMain();
            this.res.getWriter().println(Res.TRUE);
            log.info("验证码发送成功");
        } catch (Exception e) {
            log.error("验证码发送异常");
        }
    }

    /**
     * 注册
     */
    public void register() {
        String email = this.req.getParameter("email");
        String username = this.req.getParameter("username");
        String password = this.req.getParameter("password");
        String verifyCode = this.req.getParameter("verifyCode");
        try {
            if (checkVerifyCode(email, verifyCode)) {
                addUser(username, password, email);
                this.res.getWriter().println(Res.TRUE);
                log.info(email + " 注册成功");
            } else {
                this.res.getWriter().println(Res.FALSE);
                log.info(email + " 验证码错误");
            }
        } catch (Exception e) {
            log.info(email + " 注册异常");
        }
    }

    /**
     * 返回值
     */
    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }
}
