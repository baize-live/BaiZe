package top.byze.filter;

import lombok.extern.slf4j.Slf4j;
import top.byze.service.Login;
import top.byze.utils.FilterUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CodeXS
 */
@Slf4j
@WebFilter(value = {"/disk.html", "/login.html"})
public class FilterStatic implements Filter {
    private static final String LOGIN_HTML = "/login.html";
    private static final String DISK_HTML = "/disk.html";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();

        if (uri.contains(LOGIN_HTML)) {
            if (Login.isLogin(request)) {
                FilterUtil.setRedirect("/ByZe/index.html", (HttpServletResponse) res);
            } else {
                filterChain.doFilter(req, res);
            }
        } else if (uri.contains(DISK_HTML)) {
            if (Login.isLogin(request)) {
                if (Login.isOpenPan(request)) {
                    filterChain.doFilter(req, res);
                } else {
                    FilterUtil.setRedirect("/ByZe/pan.html", (HttpServletResponse) res);
                }
            } else {
                FilterUtil.setRedirect("/ByZe/login.html", (HttpServletResponse) res);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
