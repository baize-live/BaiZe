package top.byze.controller;

import lombok.extern.slf4j.Slf4j;
import top.byze.service.Login;
import top.byze.service.Register;
import top.byze.utils.MailUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CodeXS
 */
@Slf4j
@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {
    private void doException() {
        log.error("前端数据异常");
    }

    private void doWork(HttpServletRequest req, HttpServletResponse res) {
        String business = req.getParameter("business");
        if (business == null) {
            doException();
            return;
        }
        switch (business) {
            case Business.REGISTER:
                register(req, res);
                break;
            case Business.CHECK_EMAIL:
                checkEmail(req, res);
                break;
            case Business.SEND_VERIFICATION_CODE:
                sendVerifyCode(req, res);
                break;
            case Business.LOGIN:
                login(req, res);
                break;
            case Business.IS_LOGIN:
                isLogin(req, res);
                break;
            default:
                log.error("前端数据异常");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

    /**
     * 检查邮箱
     */
    public void checkEmail(HttpServletRequest req, HttpServletResponse res) {
        try {
            String email = req.getParameter("email");
            // 判断是否存在此邮箱
            if (Register.checkEmail(email)) {
                res.getWriter().println(Res.FALSE);
                log.info("此邮箱已被注册");
            } else {
                res.getWriter().println(Res.TRUE);
                log.info("此邮箱未被注册");
            }
        } catch (Exception e) {
            log.error("这里有异常");
        }
    }

    /**
     * 发送验证码
     */
    public void sendVerifyCode(HttpServletRequest req, HttpServletResponse res) {
        // 获取注册码
        String email = req.getParameter("email");
        String verifyCode = Register.generateVerifyCode();
        // 保存验证码
        Register.saveVerifyCode(email, verifyCode);
        // 发送邮件
        try {
            new MailUtil(email, verifyCode).sendMain();
            res.getWriter().println(Res.TRUE);
            log.info("验证码发送成功");
        } catch (Exception e) {
            log.error("验证码发送异常");
        }
    }

    /**
     * 注册
     */
    public void register(HttpServletRequest req, HttpServletResponse res) {
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String verifyCode = req.getParameter("verifyCode");
        try {
            if (Register.checkVerifyCode(email, verifyCode)) {
                Register.addUser(username, password, email);
                res.getWriter().println(Res.TRUE);
                log.info(email + " 注册成功");
            } else {
                res.getWriter().println(Res.FALSE);
                log.info(email + " 验证码错误");
            }
        } catch (Exception e) {
            log.info(email + " 注册异常");
        }
    }

    /**
     * 登录
     */
    public void login(HttpServletRequest req, HttpServletResponse res) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        boolean flag = Login.findUser(email, password);
        try {
            if (flag) {
                Login.addCookies(email, password, res);
                Login.setSession(email, password, req);
                res.getWriter().println(Res.TRUE);
                log.info(email + " 登录成功");
            } else {
                res.getWriter().println(Res.FALSE);
                log.info(email + " 登录失败");
            }
        } catch (Exception e) {
            log.error("登录异常");
        }
    }

    /**
     * 是否登录
     */
    public void isLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            if (Login.isLogin(req)) {
                res.getWriter().println(Res.TRUE);
            } else {
                res.getWriter().println(Res.FALSE);
            }
        } catch (Exception e) {
            log.error("退出登录异常");
        }
    }

    /**
     * 业务类型
     */
    private static class Business {
        final static String CHECK_EMAIL = "101";
        final static String SEND_VERIFICATION_CODE = "102";
        final static String REGISTER = "103";
        final static String LOGIN = "104";
        final static String IS_LOGIN = "110";
    }

    /**
     * 返回值
     */
    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }
}
