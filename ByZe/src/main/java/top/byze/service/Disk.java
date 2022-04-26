package top.byze.service;


import lombok.extern.slf4j.Slf4j;
import top.byze.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class Disk {
    HttpServletRequest req;
    HttpServletResponse res;
    PrintWriter writer = null;

    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

    public Disk(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

    public void hello() {
        HttpSession session = this.req.getSession();
        User user = (User) session.getAttribute("user");
        writer.println("<h1> Hello " + user.getUsername() + "<h1>");
        writer.println("<h1> 成功了 " + user + "<h1>");
    }

    public void world() {


    }

}

