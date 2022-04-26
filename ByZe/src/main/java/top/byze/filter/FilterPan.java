package top.byze.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Slf4j
@WebFilter(value = {"/byzepan", "/pan.html"})
public class FilterPan implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to byzepan");
        FilterTool.setEncode(req, res);
        FilterTool.setRedirect(req, res, filterChain);
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
