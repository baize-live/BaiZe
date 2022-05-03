package top.byze.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@WebServlet(name = "LoginAfter", value = "/loginAfter")
public class LoginAfter extends HttpServlet {
    // 业务类型
    private static class Business {
        final static String IsOpenPan = "105";
        final static String OpenPan = "106";
        final static String IsOpenYou = "107";
        final static String OpenYou = "108";
        final static String LOGOUT = "109";
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
            case Business.IsOpenPan:
                new top.byze.service.Login(req, res).isOpenPan();
                break;
            case Business.OpenPan:
                new top.byze.service.Login(req, res).openPan();
                break;
            case Business.IsOpenYou:
                new top.byze.service.Login(req, res).isOpenYou();
                break;
            case Business.OpenYou:
                new top.byze.service.Login(req, res).openYou();
                break;
            case Business.LOGOUT:
                new top.byze.service.Login(req, res).logout();
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
