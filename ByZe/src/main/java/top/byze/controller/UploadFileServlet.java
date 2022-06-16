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
@WebServlet(value = "/uploadFile")
public class UploadFileServlet extends HttpServlet {

    private void doWork(HttpServletRequest req, HttpServletResponse res) {
        new top.byze.service.Disk(req, res).uploadFile();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

}
