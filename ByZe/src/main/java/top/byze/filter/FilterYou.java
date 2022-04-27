package top.byze.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Slf4j
@WebFilter(value = {"/byzeyou", "/you.html"})
public class FilterYou implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to byzeyou");
        FilterTool.setEncode(req, res);
        FilterTool.redirectLogin(req, res, filterChain);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("byzeyou 成功创建");
    }

    @Override
    public void destroy() {
        log.info("byzeyou 成功摧毁");
    }
}