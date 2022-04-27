package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.mapper.UserMapper;
import top.byze.utils.CookieUtil;
import top.byze.utils.MyBatis;
import top.byze.utils.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class Login {
    HttpServletRequest req;
    HttpServletResponse res;
    PrintWriter writer = null;

    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

    public Login(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

    // 添加cookies
    private void addCookies(String email, String password) {
        User user = new User(password, email);
        CookieUtil.set(user, this.res);
    }

    // 设置session
    private void setSession(String email, String password) {
        User user = new User(password, email);
        SessionUtil.set(user, this.req);
    }

    private boolean isOpenPan(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.isOpenPan(email);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查是否开通网盘异常");
        }
        return flag;
    }

    private boolean isOpenYou(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.isOpenYou(email);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查是否开通游戏异常");
        }
        return flag;
    }

    private void openPan(String email) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.openPan(email);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("开通网盘异常");
        }
    }

    private void openYou(String email) {
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.openYou(email);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("开通游戏异常");
        }
    }

    // 查找用户
    public static boolean findUser(String email, String password) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.findUser(email, password);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("查找用户异常");
        }
        return flag;
    }

    public void login() {
        String email = this.req.getParameter("email");
        String password = this.req.getParameter("password");
        boolean flag = findUser(email, password);
        try {
            if (flag) {
                addCookies(email, password);
                setSession(email, password);
                writer.println(Res.TRUE);
                log.info("用户 " + email + " 登录成功");
            } else {
                writer.println(Res.FALSE);
                log.info("用户 " + email + " 登录失败");
            }
        } catch (Exception e) {
            writer.println(Res.FALSE);
            log.error("登录异常");
        }
        writer.close();
    }

    public void isOpenPan() {
        // 查询数据库 判断是否开启白泽库
        HttpSession session = this.req.getSession();
        User user = (User) session.getAttribute("user");
        boolean flag = isOpenPan(user.getEmail());
        if (flag) {
            writer.println(Res.TRUE);
            log.info(user.getEmail() + "网盘已经开通");
        } else {
            writer.println(Res.FALSE);
            log.info(user.getEmail() + "网盘暂未开通");
        }
    }

    public void isOpenYou() {
        // 查询数据库 判断是否开启白泽库
        HttpSession session = this.req.getSession();
        User user = (User) session.getAttribute("user");
        boolean flag = isOpenYou(user.getEmail());
        if (flag) {
            writer.println(Res.TRUE);
            log.info(user.getEmail() + "游戏已经开通");
        } else {
            writer.println(Res.FALSE);
            log.info(user.getEmail() + "游戏暂未开通");
        }
    }

    public void openPan() {
        HttpSession session = this.req.getSession();
        User user = (User) session.getAttribute("user");
        openPan(user.getEmail());
    }

    public void openYou() {
        HttpSession session = this.req.getSession();
        User user = (User) session.getAttribute("user");
        openYou(user.getEmail());
    }

    public void logout() {

    }
}
