package top.byze.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CodeXS
 */
@Slf4j
@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

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
            case Business.IS_OPEN_PAN:
                new top.byze.service.Login(req, res).isOpenPan();
                break;
            case Business.OPEN_PAN:
                new top.byze.service.Login(req, res).openPan();
                break;
            case Business.IS_OPEN_YOU:
                new top.byze.service.Login(req, res).isOpenYou();
                break;
            case Business.OPEN_YOU:
                new top.byze.service.Login(req, res).openYou();
                break;
            case Business.LOGOUT:
                new top.byze.service.Login(req, res).logout();
                break;
            default:
                doException();
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

    /**
     * 业务类型
     */
    private static class Business {
        final static String IS_OPEN_PAN = "105";
        final static String OPEN_PAN = "106";
        final static String IS_OPEN_YOU = "107";
        final static String OPEN_YOU = "108";
        final static String LOGOUT = "109";
    }

}
