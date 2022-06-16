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
@WebServlet(value = "/disk")
public class DiskServlet extends HttpServlet {

    private void doException() {
        log.error("前端数据异常");
    }

    private void doWork(HttpServletRequest req, HttpServletResponse res) {
        String business = req.getParameter("business");
        log.info("业务: " + business);
        if (business == null) {
            doException();
            return;
        }

        switch (business) {
            case Business.INIT_DATA:
                new top.byze.service.Disk(req, res).initData();
                break;
            case Business.DOWNLOAD_FILE:
                new top.byze.service.Disk(req, res).downloadFile();
                break;
            case Business.UPDATE_FILE_LIST:
                new top.byze.service.Disk(req, res).updateFileList();
                break;
            case Business.DELETE_FILE:
                new top.byze.service.Disk(req, res).deleteFile();
                break;
            case Business.LOOKUP_BIN:
                new top.byze.service.Disk(req, res).lookupBin();
                break;
            case Business.CLEAR_BIN:
                new top.byze.service.Disk(req, res).clearBin();
                break;
            case Business.CLEAR_USER_FILE:
                new top.byze.service.Disk(req, res).clearUserFile();
                break;
            case Business.RECOVERY_FILE:
                new top.byze.service.Disk(req, res).recoveryFile();
                break;
            case Business.ATTRIBUTES:
                new top.byze.service.Disk(req, res).attributes();
                break;
            case Business.GET_FRIEND:
                new top.byze.service.Disk(req, res).getFriend();
                break;
            case Business.MODIFY_ATTRIBUTES:
                new top.byze.service.Disk(req, res).modifyAttributes();
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
        final static String INIT_DATA = "201";
        final static String DOWNLOAD_FILE = "202";
        final static String UPDATE_FILE_LIST = "203";
        final static String DELETE_FILE = "204";

        final static String LOOKUP_BIN = "205";
        final static String CLEAR_BIN = "206";
        final static String CLEAR_USER_FILE = "207";
        final static String RECOVERY_FILE = "208";
        final static String ATTRIBUTES = "209";
        final static String GET_FRIEND = "210";
        final static String MODIFY_ATTRIBUTES = "211";
    }

}
