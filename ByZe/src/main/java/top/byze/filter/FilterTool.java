package top.byze.filter;

import lombok.extern.slf4j.Slf4j;
import top.byze.bean.User;
import top.byze.service.Login;
import top.byze.utils.CookieUtil;
import top.byze.utils.SessionUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
public class FilterTool {
    static final String charset = "UTF-8";
    static final String contentType = "text/html;charset=UTF-8";

    public static void setEncode(ServletRequest req, ServletResponse res) throws UnsupportedEncodingException {
        req.setCharacterEncoding(charset);
        res.setContentType(contentType);
    }

    public static void redirectLogin(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String url = "/ByZe/login.html";
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        boolean flag = false;

        if (SessionUtil.getUser(request) == null) {
            Map<String, String> cookies = CookieUtil.get((HttpServletRequest) req);
            if (cookies != null) {
                String email = cookies.get("email");
                String password = cookies.get("password");
                if (Login.findUser(email, password)) {
                    SessionUtil.set(new User(password, email), request);
                    flag = true;
                }
            }
        } else {
            flag = true;
        }

        if (flag) {
            filterChain.doFilter(req, res);
        } else {
            log.info("重定向至==>" + url);
            response.sendRedirect(url);
        }
    }

    public static void redirectIndex(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String url = "/ByZe/index.html";
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        boolean flag = false;

        if (SessionUtil.getUser(request) == null) {
            Map<String, String> cookies = CookieUtil.get((HttpServletRequest) req);
            if (cookies != null) {
                String email = cookies.get("email");
                String password = cookies.get("password");
                if (Login.findUser(email, password)) {
                    SessionUtil.set(new User(password, email), request);
                    flag = true;
                }
            }
        } else {
            flag = true;
        }

        if (flag) {
            log.info("重定向至==>" + url);
            response.sendRedirect(url);
        } else {
            filterChain.doFilter(req, res);
        }
    }

}
