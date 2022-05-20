package top.byze.filter;

import lombok.extern.slf4j.Slf4j;
import top.byze.utils.FilterUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Slf4j
@WebFilter(value = {"/disk", "/login", "/register", "/uploadFile"})
public class FilterServlet implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        FilterUtil.setEncode(req, res);
        filterChain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}