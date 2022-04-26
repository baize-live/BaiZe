package top.byze.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Slf4j
@WebFilter(value = "/byzehome")
public class FilterHome implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to byzehome");
        FilterTool.setEncode(req, res);
        filterChain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("FilterHome 成功创建");
    }

    @Override
    public void destroy() {
        log.info("FilterHome 成功摧毁");
    }
}