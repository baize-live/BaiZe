package top.byze.filter;

import lombok.extern.slf4j.Slf4j;
import top.byze.service.Login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(value = "/loginAfter")
public class loginAfter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to loginAfter");
        Tool.setEncode(req, res);

        if (Login.isLogin((HttpServletRequest) req)) {
            filterChain.doFilter(req, res);
        } else {
            Tool.setRedirect("/ByZe/login.html", (HttpServletResponse) res);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("loginAfter 成功创建");
    }

    @Override
    public void destroy() {
        log.info("loginAfter 成功摧毁");
    }
}