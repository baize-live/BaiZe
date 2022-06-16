package top.byze.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author CodeXS
 */
@Slf4j
public class FilterUtil {
    private static final String CHARSET = "UTF-8";
    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    public static void setEncode(ServletRequest req, ServletResponse res) throws UnsupportedEncodingException {
        req.setCharacterEncoding(CHARSET);
        res.setContentType(CONTENT_TYPE);
    }

    public static void setRedirect(String url, HttpServletResponse res) throws IOException {
        log.info("重定向至==>" + url);
        res.sendRedirect(url);
    }
}
