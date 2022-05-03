package top.byze.utils;

import top.byze.bean.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {
    private static final String cookieName = "loginInfo";
    private static final int cookieTime = 60*60;

    public static void set(User user, HttpServletResponse res) {
        String email = Base64.getEncoder().encodeToString(user.getEmail().getBytes());
        String password = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        Cookie cookie = new Cookie(cookieName, email + "#" + password);
        cookie.setMaxAge(cookieTime);
        res.addCookie(cookie);
    }

    public static Map<String, String> get(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                String[] params = cookie.getValue().split("#");
                String email = new String(Base64.getDecoder().decode(params[0]), StandardCharsets.UTF_8);
                String password = new String(Base64.getDecoder().decode(params[1]), StandardCharsets.UTF_8);
                map.put("email", email);
                map.put("password", password);
            }
        }
        return map;
    }

    public static void delete(HttpServletRequest request, HttpServletResponse res) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            res.addCookie(cookie);
        }
    }
}