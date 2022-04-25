package top.byze.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Slf4j
@WebFilter(value = "/byzehome")
public class ServiceFilter implements Filter {

    static final String charset = "UTF-8";
    static final String contentType = "text/html;charset=UTF-8";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request..");
        req.setCharacterEncoding(charset);
        res.setContentType(contentType);
        filterChain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
