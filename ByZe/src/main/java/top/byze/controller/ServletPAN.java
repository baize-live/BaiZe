package top.byze.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@WebServlet(name = "PAN", value = "/byzepan")
public class ServletPAN extends HttpServlet {
    // 业务类型
    private static class Business {
        final static String InitPage = "201";
        final static String UpdateUserData = "202";
        final static String UpdateFileData = "203";
    }

    // 前端数据异常
    private void doException() {
        log.error("前端数据异常");
    }

    private void doWork(HttpServletRequest req, HttpServletResponse res) {
        String business = req.getParameter("business");
        if (business == null) {
            doException();
            return;
        }
        switch (business) {
            case Business.InitPage:
                new top.byze.service.Disk(req, res).initPage();
                break;
            default:
                log.error("前端数据异常");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

}
