package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.mapper.UserMapper;
import top.byze.mapper.VerifyMapper;
import top.byze.utils.MailUtil;
import top.byze.utils.MyBatis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class Register {
    HttpServletRequest req;
    HttpServletResponse res;
    PrintWriter writer = null;

    public Register(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

    private boolean checkVerifyCode(String email, String verifyCode) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 verificationMapper 接口的代理对象
            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
            flag = verifyMapper.checkVerifyCode(email, verifyCode);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查验证码异常");
        }
        return flag;
    }

    private void saveVerifyCode(String email, String verifyCode) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 verifyMapper 接口的代理对象
            VerifyMapper verifyMapper = sqlSession.getMapper(VerifyMapper.class);
            verifyMapper.saveVerifyCode(email, verifyCode);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("保存验证码异常");
        }
    }

    private void addUser(String username, String password, String email) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = new User(username, password, "", email);
            userMapper.addUser(user);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("添加用户异常");
        }
    }

    private String generateVerifyCode() {
        // TODO: 重写生成验证码
        return "123456";
    }

    public void checkEmail() {
        MyBatis myBatis = new MyBatis();
        SqlSession sqlSession = myBatis.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        try {
            // 判断是否存在此邮箱
            boolean isExist = userMapper.checkEmail(this.req.getParameter("email"));
            if (isExist) {
                log.error("此邮箱已被注册");
                writer.println("0");
            } else {
                log.info("此邮箱未被注册");
                writer.println("1");
            }
        } catch (Exception e) {
            log.error("这里有异常");
        }
        myBatis.closeSqlSession();
        writer.close();
    }

    public void register() {
        String email = this.req.getParameter("email");
        String username = this.req.getParameter("username");
        String password = this.req.getParameter("password");
        String verifyCode = this.req.getParameter("verifyCode");
        if (checkVerifyCode(email, verifyCode)) {
            addUser(username, password, email);
            log.info(this.req.getParameter("email") + " 注册成功");
            writer.println("1");
        } else {
            writer.println("0");
            log.error(this.req.getParameter("email") + " 验证码错误");
        }
        writer.close();
    }

    public void sendVerifyCode() {
        // 获取注册码
        String email = this.req.getParameter("email");
        String verifyCode = generateVerifyCode();
        saveVerifyCode(email, verifyCode);
        // 发送邮件
        try {
            new MailUtil(email, verifyCode).sendMain();
            writer.println("1");
            log.info("验证码发送成功");
        } catch (Exception e) {
            log.error("验证码发送异常");
            writer.println("0");
        }
        writer.close();
    }
}
