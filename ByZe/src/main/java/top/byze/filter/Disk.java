package top.byze.filter;

import lombok.extern.slf4j.Slf4j;
import top.byze.service.Login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(value = {"/byzepan", "/disk.html"})
public class Disk implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to byzepan");
        Tool.setEncode(req, res);
        HttpServletRequest request = (HttpServletRequest) req;

        if (Login.isLogin(request)) {
            if (Login.isOpenPan(request)) {
                filterChain.doFilter(req, res);
            } else {
                Tool.setRedirect("/ByZe/pan.html", (HttpServletResponse) res);
            }
        } else {
            Tool.setRedirect("/ByZe/login.html", (HttpServletResponse) res);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("FilterPan 成功创建");
    }

    @Override
    public void destroy() {
        log.info("FilterPan 成功摧毁");
    }
}
