package top.byze.filter;

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

public class FilterTool {
    static final String charset = "UTF-8";
    static final String contentType = "text/html;charset=UTF-8";

    public static void setEncode(ServletRequest req, ServletResponse res) throws UnsupportedEncodingException {
        req.setCharacterEncoding(charset);
        res.setContentType(contentType);
    }

    public static void setRedirect(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> cookies = CookieUtil.get((HttpServletRequest) req);
        if (cookies != null) {
            String email = cookies.get("email");
            String password = cookies.get("password");
            if (Login.findUser(email, password)) {
                HttpServletRequest request = (HttpServletRequest) req;
                if (request.getSession() == null) {
                    SessionUtil.set(new User(password, email), request);
                }
                filterChain.doFilter(req, res);
                return;
            }
        }
        HttpServletResponse response = (HttpServletResponse) res;
        response.sendRedirect("/ByZe/login.html");
    }

}
