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

    public void logout() {

    }
}
