package top.byze.controller;

import lombok.extern.slf4j.Slf4j;
import top.byze.bean.User;
import top.byze.service.Login;
import top.byze.utils.CookieUtil;
import top.byze.utils.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CodeXS
 */
@Slf4j
@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private void doException() {
        log.error("前端数据异常");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        String business = req.getParameter("business");
        log.info("业务: " + business);
        if (business == null) {
            doException();
            return;
        }
        switch (business) {
            case Business.IS_OPEN_PAN:
                isOpenPan(req, res);
                break;
            case Business.OPEN_PAN:
                openPan(req, res);
                break;
            case Business.IS_OPEN_YOU:
                isOpenYou(req, res);
                break;
            case Business.OPEN_YOU:
                openYou(req, res);
                break;
            case Business.LOGOUT:
                logout(req, res);
                break;
            case Business.LOGIN:
                login(req, res);
                break;
            case Business.IS_LOGIN:
                isLogin(req, res);
                break;
            default:
                doException();
                break;
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
     * 返回是否开通网盘
     */
    public void isOpenPan(HttpServletRequest req, HttpServletResponse res) {
        // 查询数据库 判断是否开启白泽网盘
        User user = SessionUtil.getUser(req);
        boolean flag = Login.isOpenPan(user.getEmail());
        try {
            if (flag) {
                res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 网盘已经开通");
            } else {
                res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 网盘暂未开通");
            }
        } catch (Exception e) {
            log.error("返回是否开通网盘异常");
        }
    }

    /**
     * 返回是否开通游戏
     */
    public void isOpenYou(HttpServletRequest req, HttpServletResponse res) {
        // 查询数据库 判断是否开启白泽库
        User user = SessionUtil.getUser(req);
        boolean flag = Login.isOpenYou(user.getEmail());
        try {
            if (flag) {
                res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 游戏已经开通");
            } else {
                res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 游戏暂未开通");
            }
        } catch (Exception e) {
            log.error("返回是否开通游戏异常");
        }
    }

    /**
     * 开通网盘
     */
    public void openPan(HttpServletRequest req, HttpServletResponse res) {
        User user = SessionUtil.getUser(req);
        try {
            if (Login.openPan(user.getEmail())) {
                res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 网盘开通成功");
            } else {
                res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 网盘开通失败");
            }
        } catch (Exception e) {
            log.error("开通网盘异常");
        }
    }

    /**
     * 开通游戏
     */
    public void openYou(HttpServletRequest req, HttpServletResponse res) {
        User user = SessionUtil.getUser(req);
        try {
            if (Login.openYou(user.getEmail())) {
                res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 游戏开通成功");
            } else {
                res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 游戏开通失败");
            }
        } catch (Exception e) {
            log.error("开通游戏异常");
        }
    }

    /**
     * 登出
     */
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        // 清除cookies
        CookieUtil.delete(req, res);
        try {
            res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            log.error("退出登录异常");
        }
    }

    /**
     * 返回值
     */
    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

    /**
     * 业务类型
     */
    private static class Business {
        final static String LOGIN = "104";
        final static String IS_OPEN_PAN = "105";
        final static String OPEN_PAN = "106";
        final static String IS_OPEN_YOU = "107";
        final static String OPEN_YOU = "108";
        final static String LOGOUT = "109";
        final static String IS_LOGIN = "110";
    }
}
