package top.byze.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@WebFilter(value = "/byzehome")
public class ServiceFilter implements Filter {

    static final String charset = "UTF-8";
    static final String contentType = "text/html;charset=UTF-8";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        log.info("one request to byzehome");
        req.setCharacterEncoding(charset);
        res.setContentType(contentType);
        /// TODO： 网盘 和 游戏 需要cookies
        filterChain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("ServiceFilter 成功创建");
    }

    @Override
    public void destroy() {
        log.info("ServiceFilter 成功摧毁");
    }
}
