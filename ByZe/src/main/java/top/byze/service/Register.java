package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.mapper.UserMapper;
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
        this.res.setContentType("text/html;charset=utf-8");
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

//    private boolean checkVerification() {
//        boolean flag = false;
//        try {
//            MyBatis myBatis = new MyBatis();
//            SqlSession sqlSession = myBatis.getSqlSession();
//            // 获取 verificationMapper 接口的代理对象
//            VerificationMapper verificationMapper = sqlSession.getMapper(VerificationMapper.class);
//            String email = this.req.getParameter("email");
//            String verificationCode = this.req.getParameter("verificationCode");
//            flag = verificationMapper.checkVerification(email, verificationCode);
//            myBatis.closeSqlSession();
//        } catch (Exception e) {
//            log.error("检查验证码异常");
//            log.error(e.toString());
//        }
//        return flag;
//    }

    private static String generateVerificationCode() {
        return "123456";
    }

    private void addUser() {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            String username = this.req.getParameter("username");
            String password = this.req.getParameter("password");
            String email = this.req.getParameter("email");
            User user = new User(username, password, "", email);
            userMapper.addUser(user);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("添加用户异常");
            log.error(e.toString());
        }
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
                if (writer != null) {
                    writer.println("0");
                }
            } else {
                log.info("此邮箱未被注册");
                if (writer != null) {
                    writer.println("1");
                }
            }
        } catch (Exception e) {
            log.error("出异常了");
            log.error(e.toString());
        }
        myBatis.closeSqlSession();
        writer.close();
    }

//    public void register() {
//        if (checkVerification()) {
//            addUser();
//            log.info(this.req.getParameter("email") + " 注册成功");
//            writer.println("1");
//        } else {
//            writer.println("0");
//        }
//        writer.close();
//    }

//    public void sendVerificationCode() {
//        // 获取注册码
//        String email = this.req.getParameter("email");
//        String verificationCode = generateVerificationCode();
//        // 保存在数据库
//        MyBatis myBatis = new MyBatis();
//        SqlSession sqlSession = myBatis.getSqlSession();
//        VerificationMapper verificationMapper = sqlSession.getMapper(VerificationMapper.class);
//        try {
//            Verification verification = new Verification(email, verificationCode);
//            verificationMapper.addVerification(verification);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("验证码生成失败");
//        }
//        // 发送邮件
//        try {
//            new MailUtil(email, verificationCode).sendMain();
//            writer.println("1");
//        } catch (Exception e) {
//            log.error("验证码发送异常");
//            writer.println("0");
//        }
//    }

}
