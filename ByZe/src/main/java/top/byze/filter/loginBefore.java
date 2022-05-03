package top.byze.filter;

import lombok.extern.slf4j.Slf4j;
import top.byze.service.Login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(value = {"/loginBefore", "/login.html"})
public class loginBefore implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to loginBefore");
        Tool.setEncode(req, res);

        if (((HttpServletRequest) req).getRequestURI().contains("/login.html")) {
            if (Login.isLogin((HttpServletRequest) req)) {
                Tool.setRedirect("/ByZe/index.html", (HttpServletResponse) res);
            } else {
                filterChain.doFilter(req, res);
            }
        } else {
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("loginBefore 成功创建");
    }

    @Override
    public void destroy() {
        log.info("loginBefore 成功摧毁");
    }
}