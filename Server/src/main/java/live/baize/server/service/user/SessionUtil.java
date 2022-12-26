package live.baize.server.service.user;

import live.baize.server.bean.business.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SessionUtil {

    private final String Cookie_Name = "loginInfo";

    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;

    /**
     * 添加cookies
     */
    public void setCookies(String email, String password) {
        email = Base64.getEncoder().encodeToString(email.getBytes());
        password = Base64.getEncoder().encodeToString(password.getBytes());
        Cookie cookie = new Cookie(Cookie_Name, email + "#" + password);
        cookie.setMaxAge(2592000);
        response.addCookie(cookie);
    }

    /**
     * 设置session
     */
    public void setSession(String email) {
        request.getSession().setAttribute("user", new User(email));
    }

    /**
     * 删除cookies
     */
    public void delCookies() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 删除cookies
     */
    public void delSession() {
        request.getSession().setAttribute("user", null);
    }

    /**
     * 从Cookies中拿到User
     */
    public User getUserFromCookies() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        User user = null;
        for (Cookie cookie : cookies) {
            if (Cookie_Name.equals(cookie.getName())) {
                String[] params = cookie.getValue().split("#");
                user = new User(new String(Base64.getDecoder().decode(params[0]), StandardCharsets.UTF_8))
                        .setPassword(new String(Base64.getDecoder().decode(params[1]), StandardCharsets.UTF_8));
            }
        }
        return user;
    }

    /**
     * 从Session中拿到User
     */
    public User getUserFromSession() {
        return (User) request.getSession().getAttribute("user");
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
