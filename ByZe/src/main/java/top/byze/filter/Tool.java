package top.byze.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
public class Tool {
    static final String charset = "UTF-8";
    static final String contentType = "text/html;charset=UTF-8";

    public static void setEncode(ServletRequest req, ServletResponse res) throws UnsupportedEncodingException {
        req.setCharacterEncoding(charset);
        res.setContentType(contentType);
    }

    public static void setRedirect(String url, HttpServletResponse res) throws IOException {
        log.info("重定向至==>" + url);
        res.sendRedirect(url);
    }
}
