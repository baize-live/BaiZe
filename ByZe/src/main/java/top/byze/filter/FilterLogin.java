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
@WebFilter(value = "/login")
public class FilterLogin implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        if (Login.isLogin((HttpServletRequest) req)) {
            filterChain.doFilter(req, res);
        } else {
            FilterUtil.setRedirect("/ByZe/login.html", (HttpServletResponse) res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}