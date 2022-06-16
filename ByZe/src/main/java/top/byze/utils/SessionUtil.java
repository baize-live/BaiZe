package top.byze.utils;

import top.byze.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author CodeXS
 */
public class SessionUtil {
    /**
     * 设置session的工具类
     *
     * @param user 用户对象
     * @param req  一个http请求
     */
    public static void set(User user, HttpServletRequest req) {
        req.getSession().setAttribute("user", user);
    }

    /**
     * 获得session中的用户
     *
     * @param req 一个http请求
     */
    public static User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return ((User) session.getAttribute("user"));
    }

    /**
     * 删除session
     *
     * @param req 一个http请求
     */
    public static void delete(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.getId();
    }
}
