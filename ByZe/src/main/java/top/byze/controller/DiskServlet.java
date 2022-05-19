package top.byze.controller;

import lombok.extern.slf4j.Slf4j;
import top.byze.utils.FilterUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@WebServlet(value = "/disk")
public class DiskServlet extends HttpServlet {
    // 业务类型
    private static class Business {
        final static String initData = "201";
        final static String downloadFile = "202";
        final static String updateFileList = "203";
        final static String deleteFile = "204";
        final static String lookupBin = "205";
        final static String clearBin = "206";
        final static String clearUserFile = "207";
        final static String recoveryFile = "208";
        final static String attributes = "209";
        final static String getFriend = "210";
        final static String modifyAttributes = "211";

    }

    // 前端数据异常
    private void doException() {
        log.error("前端数据异常");
    }

    private void doWork(HttpServletRequest req, HttpServletResponse res) {
        String business = req.getParameter("business");
        System.out.println("业务: " + business);
        if (business == null) {
            doException();
            return;
        }

        switch (business) {
            case Business.initData:
                new top.byze.service.Disk(req, res).initData();
                break;
            case Business.downloadFile:
                new top.byze.service.Disk(req, res).downloadFile();
                break;
            case Business.updateFileList:
                new top.byze.service.Disk(req, res).updateFileList();
                break;
            case Business.deleteFile:
                new top.byze.service.Disk(req, res).deleteFile();
                break;
            case Business.lookupBin:
                new top.byze.service.Disk(req, res).lookupBin();
                break;
            case Business.clearBin:
                new top.byze.service.Disk(req, res).clearBin();
                break;
            case Business.clearUserFile:
                new top.byze.service.Disk(req, res).clearUserFile();
                break;
            case Business.recoveryFile:
                new top.byze.service.Disk(req, res).recoveryFile();
                break;
            case Business.attributes:
                new top.byze.service.Disk(req, res).Attributes();
                break;
            case Business.getFriend:
                new top.byze.service.Disk(req, res).getFriend();
                break;
            case Business.modifyAttributes:
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

}
